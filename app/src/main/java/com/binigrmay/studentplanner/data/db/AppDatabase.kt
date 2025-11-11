package com.binigrmay.studentplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.binigrmay.studentplanner.data.model.Lecture
import com.binigrmay.studentplanner.data.model.Task

/**
 * Main Room Database for StudentPlanner app
 */
@Database(
    entities = [Task::class, Lecture::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun taskDao(): TaskDao
    abstract fun lectureDao(): LectureDao
    
    companion object {
        const val DATABASE_NAME = "student_planner_db"
    }
}
