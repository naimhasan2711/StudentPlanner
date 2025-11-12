package com.binigrmay.studentplanner.utils

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.binigrmay.studentplanner.data.model.DayOfWeek
import com.binigrmay.studentplanner.data.model.Lecture
import com.binigrmay.studentplanner.data.model.Task
import com.binigrmay.studentplanner.worker.LectureReminderWorker
import com.binigrmay.studentplanner.worker.RecurringLectureReminderWorker
import com.binigrmay.studentplanner.worker.TaskReminderWorker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Helper class for scheduling reminder notifications
 */
object ReminderScheduler {
    
    private const val TAG = "ReminderScheduler"
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
    
    /**
     * Schedule a reminder for a task
     */
    fun scheduleTaskReminder(context: Context, task: Task) {
        Log.d(TAG, "========== SCHEDULING TASK REMINDER ==========")
        Log.d(TAG, "Task ID: ${task.id}")
        Log.d(TAG, "Task Title: ${task.title}")
        Log.d(TAG, "Reminder Enabled: ${task.reminderEnabled}")
        Log.d(TAG, "Reminder Time (minutes): ${task.reminderTime}")
        
        if (!task.reminderEnabled || task.reminderTime == null) {
            Log.d(TAG, "Reminder not enabled or reminder time is null. Skipping.")
            return
        }
        
        val currentTime = System.currentTimeMillis()
        val reminderTime = task.dueDate - (task.reminderTime * 60 * 1000) // Convert minutes to milliseconds
        val delay = reminderTime - currentTime
        
        Log.d(TAG, "Current Time: ${dateFormat.format(Date(currentTime))}")
        Log.d(TAG, "Due Date: ${dateFormat.format(Date(task.dueDate))}")
        Log.d(TAG, "Reminder Time: ${dateFormat.format(Date(reminderTime))}")
        Log.d(TAG, "Delay (ms): $delay (${delay / 1000 / 60} minutes)")
        
        if (delay <= 0) {
            Log.w(TAG, "Reminder time has already passed. Not scheduling.")
            return
        }
        
        val data = Data.Builder()
            .putInt("taskId", task.id)
            .putString("title", task.title)
            .putString("description", task.description)
            .build()
        
        val workRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("${TaskReminderWorker.WORK_NAME_PREFIX}${task.id}")
            .build()
        
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "${TaskReminderWorker.WORK_NAME_PREFIX}${task.id}",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        
        Log.d(TAG, "✅ Task reminder scheduled successfully!")
        Log.d(TAG, "Work Name: ${TaskReminderWorker.WORK_NAME_PREFIX}${task.id}")
        Log.d(TAG, "Will trigger at: ${dateFormat.format(Date(reminderTime))}")
        Log.d(TAG, "============================================")
    }
    
    /**
     * Schedule a reminder for a lecture
     */
    fun scheduleLectureReminder(context: Context, lecture: Lecture) {
        Log.d(TAG, "========== SCHEDULING LECTURE REMINDER ==========")
        Log.d(TAG, "Lecture ID: ${lecture.id}")
        Log.d(TAG, "Lecture Title: ${lecture.title}")
        Log.d(TAG, "Is Recurring: ${lecture.isRecurring}")
        Log.d(TAG, "Day of Week: ${lecture.dayOfWeek}")
        Log.d(TAG, "Start Time: ${lecture.startTime}")
        Log.d(TAG, "Reminder Enabled: ${lecture.reminderEnabled}")
        Log.d(TAG, "Reminder Minutes Before: ${lecture.reminderMinutesBefore}")
        
        if (!lecture.reminderEnabled) {
            Log.d(TAG, "Reminder not enabled. Skipping.")
            return
        }
        
        if (lecture.isRecurring) {
            // For recurring lectures, use the RecurringLectureReminderWorker
            scheduleRecurringLectureReminder(context, lecture)
        } else {
            // For one-time lectures, use the regular LectureReminderWorker
            scheduleOneTimeLectureReminder(context, lecture)
        }
        
        Log.d(TAG, "==============================================")
    }
    
    /**
     * Schedule a recurring lecture reminder (weekly)
     */
    private fun scheduleRecurringLectureReminder(context: Context, lecture: Lecture) {
        Log.d(TAG, "Scheduling RECURRING lecture reminder...")
        
        val nextReminderTime = calculateNextReminderTime(
            lecture.dayOfWeek,
            lecture.startTime,
            lecture.reminderMinutesBefore
        )
        val currentTime = System.currentTimeMillis()
        val delay = nextReminderTime - currentTime
        
        Log.d(TAG, "Current Time: ${dateFormat.format(Date(currentTime))}")
        Log.d(TAG, "Next Reminder Time: ${dateFormat.format(Date(nextReminderTime))}")
        Log.d(TAG, "Delay (ms): $delay (${delay / 1000 / 60} minutes)")
        
        if (delay <= 0) {
            Log.w(TAG, "Calculated delay is negative or zero. Not scheduling.")
            return
        }
        
        val data = Data.Builder()
            .putInt("lectureId", lecture.id)
            .putString("title", lecture.title)
            .putString("time", lecture.startTime)
            .putString("room", lecture.room)
            .putString("dayOfWeek", lecture.dayOfWeek.name)
            .putInt("reminderMinutesBefore", lecture.reminderMinutesBefore)
            .build()
        
        val workRequest = OneTimeWorkRequestBuilder<RecurringLectureReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("${RecurringLectureReminderWorker.WORK_NAME_PREFIX}${lecture.id}")
            .build()
        
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "${RecurringLectureReminderWorker.WORK_NAME_PREFIX}${lecture.id}",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        
        Log.d(TAG, "✅ Recurring lecture reminder scheduled successfully!")
        Log.d(TAG, "Work Name: ${RecurringLectureReminderWorker.WORK_NAME_PREFIX}${lecture.id}")
        Log.d(TAG, "Next occurrence: ${dateFormat.format(Date(nextReminderTime))}")
    }
    
    /**
     * Schedule a one-time lecture reminder
     */
    private fun scheduleOneTimeLectureReminder(context: Context, lecture: Lecture) {
        Log.d(TAG, "Scheduling ONE-TIME lecture reminder...")
        
        if (lecture.specificDate == null) {
            Log.w(TAG, "One-time lecture but specificDate is null. Cannot schedule.")
            return
        }
        
        val currentTime = System.currentTimeMillis()
        val reminderTime = lecture.specificDate - (lecture.reminderMinutesBefore * 60 * 1000L)
        val delay = reminderTime - currentTime
        
        Log.d(TAG, "Current Time: ${dateFormat.format(Date(currentTime))}")
        Log.d(TAG, "Specific Date: ${dateFormat.format(Date(lecture.specificDate))}")
        Log.d(TAG, "Reminder Time: ${dateFormat.format(Date(reminderTime))}")
        Log.d(TAG, "Delay (ms): $delay (${delay / 1000 / 60} minutes)")
        
        if (delay <= 0) {
            Log.w(TAG, "Reminder time has already passed. Not scheduling.")
            return
        }
        
        val data = Data.Builder()
            .putInt("lectureId", lecture.id)
            .putString("title", lecture.title)
            .putString("time", lecture.startTime)
            .putString("room", lecture.room)
            .build()
        
        val workRequest = OneTimeWorkRequestBuilder<LectureReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("${LectureReminderWorker.WORK_NAME_PREFIX}${lecture.id}")
            .build()
        
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "${LectureReminderWorker.WORK_NAME_PREFIX}${lecture.id}",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        
        Log.d(TAG, "✅ One-time lecture reminder scheduled successfully!")
        Log.d(TAG, "Work Name: ${LectureReminderWorker.WORK_NAME_PREFIX}${lecture.id}")
        Log.d(TAG, "Will trigger at: ${dateFormat.format(Date(reminderTime))}")
    }
    
    /**
     * Calculate the next reminder time for a recurring lecture
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
        
        // If it's the same day, check if the time has passed
        if (daysUntilTarget == 0) {
            val lectureCalendar = Calendar.getInstance()
            lectureCalendar.set(Calendar.HOUR_OF_DAY, hour)
            lectureCalendar.set(Calendar.MINUTE, minute)
            lectureCalendar.set(Calendar.SECOND, 0)
            lectureCalendar.set(Calendar.MILLISECOND, 0)
            lectureCalendar.add(Calendar.MINUTE, -reminderMinutesBefore)
            
            // If reminder time has passed today, schedule for next week
            if (lectureCalendar.timeInMillis <= System.currentTimeMillis()) {
                daysUntilTarget = 7
            }
        } else if (daysUntilTarget < 0) {
            // If target day has passed this week, go to next week
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
    
    /**
     * Cancel a task reminder
     */
    fun cancelTaskReminder(context: Context, taskId: Int) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("${TaskReminderWorker.WORK_NAME_PREFIX}$taskId")
    }
    
    /**
     * Cancel a lecture reminder
     */
    fun cancelLectureReminder(context: Context, lectureId: Int) {
        // Cancel both types of lecture reminders
        WorkManager.getInstance(context)
            .cancelUniqueWork("${LectureReminderWorker.WORK_NAME_PREFIX}$lectureId")
        WorkManager.getInstance(context)
            .cancelUniqueWork("${RecurringLectureReminderWorker.WORK_NAME_PREFIX}$lectureId")
    }
}
