package com.binigrmay.studentplanner.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.binigrmay.studentplanner.data.model.Lecture
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Lecture entity
 */
@Dao
interface LectureDao {
    
    @Query("SELECT * FROM lectures ORDER BY dayOfWeek, startTime ASC")
    fun getAllLectures(): Flow<List<Lecture>>
    
    @Query("SELECT * FROM lectures WHERE id = :lectureId")
    suspend fun getLectureById(lectureId: Int): Lecture?
    
    @Query("SELECT * FROM lectures WHERE isRecurring = 1 ORDER BY dayOfWeek, startTime ASC")
    fun getRecurringLectures(): Flow<List<Lecture>>
    
    @Query("SELECT * FROM lectures WHERE isRecurring = 0 ORDER BY specificDate ASC")
    fun getOneTimeLectures(): Flow<List<Lecture>>
    
    @Query("""
        SELECT * FROM lectures 
        WHERE isRecurring = 1 AND dayOfWeek = :dayOfWeek 
        ORDER BY startTime ASC
    """)
    fun getLecturesForDay(dayOfWeek: String): Flow<List<Lecture>>
    
    @Query("""
        SELECT * FROM lectures 
        WHERE isRecurring = 0 
        AND specificDate >= :startOfDay 
        AND specificDate < :endOfDay
        ORDER BY startTime ASC
    """)
    fun getOneTimeLecturesForDate(startOfDay: Long, endOfDay: Long): Flow<List<Lecture>>
    
    @Query("""
        SELECT * FROM lectures 
        WHERE title LIKE '%' || :query || '%' 
        OR instructor LIKE '%' || :query || '%'
        OR room LIKE '%' || :query || '%'
        ORDER BY dayOfWeek, startTime ASC
    """)
    fun searchLectures(query: String): Flow<List<Lecture>>
    
    @Query("SELECT * FROM lectures WHERE instructor = :instructorName ORDER BY dayOfWeek, startTime ASC")
    fun getLecturesByInstructor(instructorName: String): Flow<List<Lecture>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecture(lecture: Lecture): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectures(lectures: List<Lecture>)
    
    @Update
    suspend fun updateLecture(lecture: Lecture)
    
    @Delete
    suspend fun deleteLecture(lecture: Lecture)
    
    @Query("DELETE FROM lectures WHERE id = :lectureId")
    suspend fun deleteLectureById(lectureId: Int)
    
    @Query("DELETE FROM lectures")
    suspend fun deleteAllLectures()
}
