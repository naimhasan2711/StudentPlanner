package com.binigrmay.studentplanner.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.binigrmay.studentplanner.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker for showing task reminder notifications
 */
@HiltWorker
class TaskReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    
    companion object {
        const val WORK_NAME_PREFIX = "task_reminder_"
        private const val TAG = "TaskReminderWorker"
    }
    
    override suspend fun doWork(): Result {
        Log.d(TAG, "========== TASK REMINDER TRIGGERED ==========")
        return try {
            val taskId = inputData.getInt("taskId", -1)
            val title = inputData.getString("title") ?: "Task Reminder"
            val description = inputData.getString("description") ?: ""
            
            Log.d(TAG, "Task ID: $taskId")
            Log.d(TAG, "Title: $title")
            Log.d(TAG, "Description: $description")
            
            if (taskId != -1) {
                Log.d(TAG, "Showing notification...")
                NotificationHelper.showTaskNotification(
                    context = context,
                    taskId = taskId,
                    title = title,
                    description = description
                )
                Log.d(TAG, "✅ Notification shown successfully!")
            } else {
                Log.e(TAG, "❌ Invalid task ID: $taskId")
            }
            
            Log.d(TAG, "==========================================")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error showing notification: ${e.message}", e)
            Log.d(TAG, "==========================================")
            Result.failure()
        }
    }
}
