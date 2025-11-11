package com.binigrmay.studentplanner.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility functions for date and time operations
 */
object DateTimeUtils {
    
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    
    /**
     * Format timestamp to date string
     */
    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }
    
    /**
     * Format time string (HH:mm)
     */
    fun formatTime(time: String): String {
        return try {
            val date = timeFormat.parse(time)
            date?.let { timeFormat.format(it) } ?: time
        } catch (e: Exception) {
            time
        }
    }
    
    /**
     * Get start of day timestamp
     */
    fun getStartOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * Get end of day timestamp
     */
    fun getEndOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
    
    /**
     * Get start of week timestamp
     */
    fun getStartOfWeek(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return getStartOfDay(calendar.timeInMillis)
    }
    
    /**
     * Get end of week timestamp
     */
    fun getEndOfWeek(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = getStartOfWeek(timestamp)
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        return calendar.timeInMillis
    }
    
    /**
     * Check if date is today
     */
    fun isToday(timestamp: Long): Boolean {
        val today = getStartOfDay()
        val endOfToday = getEndOfDay()
        return timestamp in today until endOfToday
    }
    
    /**
     * Check if date is in the past
     */
    fun isPast(timestamp: Long): Boolean {
        return timestamp < System.currentTimeMillis()
    }
    
    /**
     * Check if date is in the future
     */
    fun isFuture(timestamp: Long): Boolean {
        return timestamp > System.currentTimeMillis()
    }
    
    /**
     * Get relative time string (Today, Tomorrow, etc.)
     */
    fun getRelativeTimeString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = timestamp - now
        
        return when {
            isToday(timestamp) -> "Today"
            diff in 0..86400000 -> "Tomorrow" // Within 24 hours
            diff < 0 && diff > -86400000 -> "Yesterday"
            diff in 86400000..172800000 -> "In 2 days"
            else -> formatDate(timestamp)
        }
    }
}
