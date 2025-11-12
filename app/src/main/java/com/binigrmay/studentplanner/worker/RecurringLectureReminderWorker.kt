package com.binigrmay.studentplanner.worker

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.binigrmay.studentplanner.data.model.DayOfWeek
import com.binigrmay.studentplanner.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Worker for showing recurring lecture reminder notifications
 * This worker shows the notification and then reschedules itself for next week
 */
@HiltWorker
class RecurringLectureReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    
    companion object {
        const val WORK_NAME_PREFIX = "recurring_lecture_reminder_"
        private const val TAG = "RecurringLectureWorker"
        private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
    }
    
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        return try {
            val lectureId = inputData.getInt("lectureId", -1)
            val title = inputData.getString("title") ?: "Lecture Reminder"
            val time = inputData.getString("time") ?: ""
            val room = inputData.getString("room") ?: ""
            val dayOfWeekString = inputData.getString("dayOfWeek") ?: ""
            val reminderMinutesBefore = inputData.getInt("reminderMinutesBefore", 15)
            
            Log.d(TAG, "========== RECURRING LECTURE REMINDER TRIGGERED ==========")
            Log.d(TAG, "Lecture ID: $lectureId")
            Log.d(TAG, "Title: $title")
            Log.d(TAG, "Time: $time")
            Log.d(TAG, "Day: $dayOfWeekString")
            
            if (lectureId != -1) {
                // Show the notification
                NotificationHelper.showLectureNotification(
                    context = context,
                    lectureId = lectureId,
                    title = title,
                    time = time,
                    room = room
                )
                Log.d(TAG, "✅ Notification shown successfully")
                
                // Schedule for next week
                val dayOfWeek = try {
                    DayOfWeek.valueOf(dayOfWeekString)
                } catch (e: Exception) {
                    Log.e(TAG, "Invalid day of week: $dayOfWeekString")
                    return Result.failure()
                }
                
                scheduleNextWeek(lectureId, title, time, room, dayOfWeek, reminderMinutesBefore)
            }
            
            Log.d(TAG, "========================================================")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in RecurringLectureReminderWorker", e)
            Result.failure()
        }
    }
    
    private fun scheduleNextWeek(
        lectureId: Int,
        title: String,
        time: String,
        room: String,
        dayOfWeek: DayOfWeek,
        reminderMinutesBefore: Int
    ) {
        Log.d(TAG, "Scheduling reminder for next week...")
        
        val nextReminderTime = calculateNextReminderTime(dayOfWeek, time, reminderMinutesBefore)
        val currentTime = System.currentTimeMillis()
        val delay = nextReminderTime - currentTime
        
        Log.d(TAG, "Current Time: ${dateFormat.format(Date(currentTime))}")
        Log.d(TAG, "Next Reminder Time: ${dateFormat.format(Date(nextReminderTime))}")
        Log.d(TAG, "Delay: ${delay / 1000 / 60} minutes (${delay / 1000 / 60 / 60} hours)")
        
        if (delay <= 0) {
            Log.w(TAG, "Calculated delay is negative or zero. Something went wrong.")
            return
        }
        
        val data = Data.Builder()
            .putInt("lectureId", lectureId)
            .putString("title", title)
            .putString("time", time)
            .putString("room", room)
            .putString("dayOfWeek", dayOfWeek.name)
            .putInt("reminderMinutesBefore", reminderMinutesBefore)
            .build()
        
        val workRequest = OneTimeWorkRequestBuilder<RecurringLectureReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("$WORK_NAME_PREFIX$lectureId")
            .build()
        
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "$WORK_NAME_PREFIX$lectureId",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        
        Log.d(TAG, "✅ Scheduled for next week: ${dateFormat.format(Date(nextReminderTime))}")
    }
    
    /**
     * Calculate the next reminder time based on day of week and lecture start time
     */
    private fun calculateNextReminderTime(
        dayOfWeek: DayOfWeek,
        startTime: String,
        reminderMinutesBefore: Int
    ): Long {
        val calendar = Calendar.getInstance()
        
        // Get target day of week (Calendar uses 1=Sunday, 7=Saturday)
        val targetDay = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> Calendar.SUNDAY
            DayOfWeek.MONDAY -> Calendar.MONDAY
            DayOfWeek.TUESDAY -> Calendar.TUESDAY
            DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
            DayOfWeek.THURSDAY -> Calendar.THURSDAY
            DayOfWeek.FRIDAY -> Calendar.FRIDAY
            DayOfWeek.SATURDAY -> Calendar.SATURDAY
        }
        
        // Parse start time (format: "HH:mm")
        val timeParts = startTime.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 9
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
        
        // Set to next occurrence of target day
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        var daysUntilTarget = targetDay - currentDay
        
        // If target day is today or already passed, go to next week
        if (daysUntilTarget <= 0) {
            daysUntilTarget += 7
        }
        
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilTarget)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        // Subtract reminder minutes
        calendar.add(Calendar.MINUTE, -reminderMinutesBefore)
        
        return calendar.timeInMillis
    }
}
