package com.binigrmay.studentplanner.utils

import android.content.Context
import androidx.work.*
import com.binigrmay.studentplanner.data.model.Lecture
import com.binigrmay.studentplanner.data.model.Task
import com.binigrmay.studentplanner.worker.LectureReminderWorker
import com.binigrmay.studentplanner.worker.TaskReminderWorker
import java.util.concurrent.TimeUnit

/**
 * Helper class for scheduling reminder notifications
 */
object ReminderScheduler {
    
    /**
     * Schedule a reminder for a task
     */
    fun scheduleTaskReminder(context: Context, task: Task) {
        if (!task.reminderEnabled || task.reminderTime == null) return
        
        val currentTime = System.currentTimeMillis()
        val reminderTime = task.dueDate - (task.reminderTime * 60 * 1000) // Convert minutes to milliseconds
        val delay = reminderTime - currentTime
        
        if (delay <= 0) return // Don't schedule if time has passed
        
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
    }
    
    /**
     * Schedule a reminder for a lecture
     */
    fun scheduleLectureReminder(context: Context, lecture: Lecture) {
        if (!lecture.reminderEnabled) return
        
        // For recurring lectures, we would need to calculate the next occurrence
        // For simplicity, this handles one-time lectures or requires manual scheduling
        if (lecture.specificDate != null) {
            val currentTime = System.currentTimeMillis()
            val reminderTime = lecture.specificDate - (lecture.reminderMinutesBefore * 60 * 1000L)
            val delay = reminderTime - currentTime
            
            if (delay <= 0) return // Don't schedule if time has passed
            
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
        }
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
