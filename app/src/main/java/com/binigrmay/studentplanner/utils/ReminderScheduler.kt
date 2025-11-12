package com.binigrmay.studentplanner.utils

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.binigrmay.studentplanner.data.model.Lecture
import com.binigrmay.studentplanner.data.model.Task
import com.binigrmay.studentplanner.worker.LectureReminderWorker
import com.binigrmay.studentplanner.worker.TaskReminderWorker
import java.text.SimpleDateFormat
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
        Log.d(TAG, "Reminder Enabled: ${lecture.reminderEnabled}")
        Log.d(TAG, "Reminder Minutes Before: ${lecture.reminderMinutesBefore}")
        
        if (!lecture.reminderEnabled) {
            Log.d(TAG, "Reminder not enabled. Skipping.")
            return
        }
        
        // For recurring lectures, we would need to calculate the next occurrence
        // For simplicity, this handles one-time lectures or requires manual scheduling
        if (lecture.specificDate != null) {
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
            
            Log.d(TAG, "✅ Lecture reminder scheduled successfully!")
            Log.d(TAG, "Work Name: ${LectureReminderWorker.WORK_NAME_PREFIX}${lecture.id}")
            Log.d(TAG, "Will trigger at: ${dateFormat.format(Date(reminderTime))}")
        } else {
            Log.d(TAG, "Recurring lecture - specific date is null. Reminders for recurring lectures need to be scheduled separately.")
        }
        Log.d(TAG, "==============================================")
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
        WorkManager.getInstance(context)
            .cancelUniqueWork("${LectureReminderWorker.WORK_NAME_PREFIX}$lectureId")
    }
}
