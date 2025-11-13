package com.binigrmay.studentplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.binigrmay.studentplanner.data.model.Lecture
import com.binigrmay.studentplanner.data.model.Task

/**
 * Main Room Database for StudentPlanner app
 */
@Database(
    entities = [Task::class, Lecture::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun taskDao(): TaskDao
    abstract fun lectureDao(): LectureDao
    
    companion object {
        const val DATABASE_NAME = "student_planner_db"
        
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add dueTime column to tasks table with default value "23:59"
                database.execSQL("ALTER TABLE tasks ADD COLUMN dueTime TEXT NOT NULL DEFAULT '23:59'")
            }
        }
    }
}
