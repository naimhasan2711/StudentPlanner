package com.binigrmay.studentplanner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Task entity representing a student's task or deadline
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val title: String,
    
    val description: String = "",
    
    val dueDate: Long, // Store as timestamp
    
    val priority: Priority = Priority.MEDIUM,
    
    val isCompleted: Boolean = false,
    
    val reminderEnabled: Boolean = false,
    
    val reminderTime: Long? = null, // Time before due date to remind (in minutes)
    
    val createdAt: Long = System.currentTimeMillis(),
    
    val category: String = "General"
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}
