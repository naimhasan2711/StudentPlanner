package com.binigrmay.studentplanner.data.repository

import com.binigrmay.studentplanner.data.db.LectureDao
import com.binigrmay.studentplanner.data.model.DayOfWeek
import com.binigrmay.studentplanner.data.model.Lecture
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Lecture data operations
 * Provides a clean API for data access to the rest of the app
 */
@Singleton
class LectureRepository @Inject constructor(
    private val lectureDao: LectureDao
) {
    
    fun getAllLectures(): Flow<List<Lecture>> = lectureDao.getAllLectures()
    
    fun getRecurringLectures(): Flow<List<Lecture>> = lectureDao.getRecurringLectures()
    
    fun getOneTimeLectures(): Flow<List<Lecture>> = lectureDao.getOneTimeLectures()
    
    fun getLecturesForDay(dayOfWeek: DayOfWeek): Flow<List<Lecture>> =
        lectureDao.getLecturesForDay(dayOfWeek.name)
    
    fun getOneTimeLecturesForDate(startOfDay: Long, endOfDay: Long): Flow<List<Lecture>> =
        lectureDao.getOneTimeLecturesForDate(startOfDay, endOfDay)
    
    fun searchLectures(query: String): Flow<List<Lecture>> =
        lectureDao.searchLectures(query)
    
    fun getLecturesByInstructor(instructorName: String): Flow<List<Lecture>> =
        lectureDao.getLecturesByInstructor(instructorName)
    
    suspend fun getLectureById(lectureId: Int): Lecture? =
        lectureDao.getLectureById(lectureId)
    
    suspend fun insertLecture(lecture: Lecture): Long =
        lectureDao.insertLecture(lecture)
    
    suspend fun insertLectures(lectures: List<Lecture>) =
        lectureDao.insertLectures(lectures)
    
    suspend fun updateLecture(lecture: Lecture) =
        lectureDao.updateLecture(lecture)
    
    suspend fun deleteLecture(lecture: Lecture) =
        lectureDao.deleteLecture(lecture)
    
    suspend fun deleteLectureById(lectureId: Int) =
        lectureDao.deleteLectureById(lectureId)
    
    suspend fun deleteAllLectures() =
        lectureDao.deleteAllLectures()
}
