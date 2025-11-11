package com.binigrmay.studentplanner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Lecture entity representing a student's class or lecture
 */
@Entity(tableName = "lectures")
data class Lecture(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val title: String,
    
    val instructor: String = "",
    
    val room: String = "",
    
    val dayOfWeek: DayOfWeek,
    
    val startTime: String, // Format: "HH:mm" (e.g., "09:00")
    
    val endTime: String, // Format: "HH:mm" (e.g., "10:30")
    
    val isRecurring: Boolean = true, // Most lectures are recurring weekly
    
    val specificDate: Long? = null, // For one-time lectures (timestamp)
    
    val color: String = "#6200EE", // Hex color for visual distinction
    
    val notes: String = "",
    
    val reminderEnabled: Boolean = false,
    
    val reminderMinutesBefore: Int = 15 // Default 15 minutes before
)

enum class DayOfWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;
    
    companion object {
        fun fromCalendar(calendarDay: Int): DayOfWeek {
            return when (calendarDay) {
                1 -> SUNDAY
                2 -> MONDAY
                3 -> TUESDAY
                4 -> WEDNESDAY
                5 -> THURSDAY
                6 -> FRIDAY
                7 -> SATURDAY
                else -> MONDAY
            }
        }
    }
}
