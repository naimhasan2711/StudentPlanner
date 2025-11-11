package com.binigrmay.studentplanner.data.db

import androidx.room.TypeConverter
import com.binigrmay.studentplanner.data.model.DayOfWeek
import com.binigrmay.studentplanner.data.model.Priority

/**
 * Type converters for Room Database
 */
class Converters {
    
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }
    
    @TypeConverter
    fun toPriority(value: String): Priority {
        return try {
            Priority.valueOf(value)
        } catch (e: IllegalArgumentException) {
            Priority.MEDIUM
        }
    }
    
    @TypeConverter
    fun fromDayOfWeek(dayOfWeek: DayOfWeek): String {
        return dayOfWeek.name
    }
    
    @TypeConverter
    fun toDayOfWeek(value: String): DayOfWeek {
        return try {
            DayOfWeek.valueOf(value)
        } catch (e: IllegalArgumentException) {
            DayOfWeek.MONDAY
        }
    }
}
