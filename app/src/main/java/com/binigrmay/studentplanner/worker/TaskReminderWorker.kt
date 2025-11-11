package com.binigrmay.studentplanner.worker

import android.content.Context
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
    
    override suspend fun doWork(): Result {
        return try {
            val taskId = inputData.getInt("taskId", -1)
            val title = inputData.getString("title") ?: "Task Reminder"
            val description = inputData.getString("description") ?: ""
            
            if (taskId != -1) {
                NotificationHelper.showTaskNotification(
                    context = context,
                    taskId = taskId,
                    title = title,
                    description = description
                )
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    companion object {
        const val WORK_NAME_PREFIX = "task_reminder_"
    }
}
