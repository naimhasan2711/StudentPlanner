package com.binigrmay.studentplanner.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.binigrmay.studentplanner.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker for showing lecture reminder notifications
 */
@HiltWorker
class LectureReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    
    override suspend fun doWork(): Result {
        return try {
            val lectureId = inputData.getInt("lectureId", -1)
            val title = inputData.getString("title") ?: "Lecture Reminder"
            val time = inputData.getString("time") ?: ""
            val room = inputData.getString("room") ?: ""
            
            if (lectureId != -1) {
                NotificationHelper.showLectureNotification(
                    context = context,
                    lectureId = lectureId,
                    title = title,
                    time = time,
                    room = room
                )
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    companion object {
        const val WORK_NAME_PREFIX = "lecture_reminder_"
    }
}
