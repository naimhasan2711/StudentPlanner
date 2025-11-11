package com.binigrmay.studentplanner.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.binigrmay.studentplanner.MainActivity
import com.binigrmay.studentplanner.R

/**
 * Helper class for creating and managing notifications
 */
object NotificationHelper {
    
    const val CHANNEL_ID_TASKS = "task_reminders"
    const val CHANNEL_ID_LECTURES = "lecture_reminders"
    
    private const val CHANNEL_NAME_TASKS = "Task Reminders"
    private const val CHANNEL_NAME_LECTURES = "Lecture Reminders"
    private const val CHANNEL_DESCRIPTION_TASKS = "Notifications for upcoming tasks and deadlines"
    private const val CHANNEL_DESCRIPTION_LECTURES = "Notifications for upcoming lectures"
    
    /**
     * Create notification channels (required for Android O and above)
     */
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Task reminders channel
            val taskChannel = NotificationChannel(
                CHANNEL_ID_TASKS,
                CHANNEL_NAME_TASKS,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION_TASKS
                enableVibration(true)
            }
            
            // Lecture reminders channel
            val lectureChannel = NotificationChannel(
                CHANNEL_ID_LECTURES,
                CHANNEL_NAME_LECTURES,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION_LECTURES
                enableVibration(true)
            }
            
            // Register channels
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(taskChannel)
            notificationManager.createNotificationChannel(lectureChannel)
        }
    }
    
    /**
     * Show a task reminder notification
     */
    fun showTaskNotification(
        context: Context,
        taskId: Int,
        title: String,
        description: String
    ) {
        if (!hasNotificationPermission(context)) return
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("taskId", taskId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_TASKS)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your app icon
            .setContentTitle("Task Reminder: $title")
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        NotificationManagerCompat.from(context).notify(taskId, notification)
    }
    
    /**
     * Show a lecture reminder notification
     */
    fun showLectureNotification(
        context: Context,
        lectureId: Int,
        title: String,
        time: String,
        room: String
    ) {
        if (!hasNotificationPermission(context)) return
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("lectureId", lectureId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            lectureId + 10000, // Offset to avoid collision with task notifications
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_LECTURES)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your app icon
            .setContentTitle("Lecture: $title")
            .setContentText("Starts at $time in $room")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        NotificationManagerCompat.from(context).notify(lectureId + 10000, notification)
    }
    
    /**
     * Check if the app has notification permission
     */
    private fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
    
    /**
     * Cancel a specific notification
     */
    fun cancelNotification(context: Context, notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }
}
