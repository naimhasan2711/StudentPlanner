package com.binigrmay.studentplanner.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binigrmay.studentplanner.data.model.DayOfWeek
import com.binigrmay.studentplanner.data.model.Lecture
import com.binigrmay.studentplanner.data.repository.LectureRepository
import com.binigrmay.studentplanner.utils.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for managing Lecture-related UI state and operations
 */
@HiltViewModel
class LectureViewModel @Inject constructor(
    private val repository: LectureRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    companion object {
        private const val TAG = "LectureViewModel"
    }
    
    // All lectures
    val allLectures: StateFlow<List<Lecture>> = repository.getAllLectures()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Today's lectures
    private val _todaysLectures = MutableStateFlow<List<Lecture>>(emptyList())
    val todaysLectures: StateFlow<List<Lecture>> = _todaysLectures.asStateFlow()
    
    // Selected day lectures
    private val _selectedDayLectures = MutableStateFlow<List<Lecture>>(emptyList())
    val selectedDayLectures: StateFlow<List<Lecture>> = _selectedDayLectures.asStateFlow()
    
    // Search query
    private val _searchQuery = MutableStateFlow("")

    init {
        loadTodaysLectures()
    }
    
    private fun loadTodaysLectures() {
        viewModelScope.launch {
            val today = Calendar.getInstance()
            val todayDayOfWeek = DayOfWeek.fromCalendar(today.get(Calendar.DAY_OF_WEEK))
            
            // Get recurring lectures for today
            repository.getLecturesForDay(todayDayOfWeek)
                .combine(getOneTimeLecturesForToday()) { recurring, oneTime ->
                    recurring + oneTime
                }
                .collect { lectures ->
                    _todaysLectures.value = lectures.sortedBy { it.startTime }
                }
        }
    }
    
    private fun getOneTimeLecturesForToday(): Flow<List<Lecture>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis
        
        return repository.getOneTimeLecturesForDate(startOfDay, endOfDay)
    }
    
    fun loadLecturesForDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            repository.getLecturesForDay(dayOfWeek)
                .collect { lectures ->
                    _selectedDayLectures.value = lectures
                }
        }
    }
    
    fun insertLecture(lecture: Lecture) {
        viewModelScope.launch {
            Log.d(TAG, "Inserting lecture: ${lecture.title}")
            val lectureId = repository.insertLecture(lecture)
            Log.d(TAG, "Lecture inserted with ID: $lectureId")
            
            // Schedule reminder if enabled
            if (lecture.reminderEnabled && lectureId > 0) {
                val lectureWithId = lecture.copy(id = lectureId.toInt())
                Log.d(TAG, "Scheduling reminder for lecture: ${lectureWithId.title}")
                ReminderScheduler.scheduleLectureReminder(context, lectureWithId)
            }
        }
    }
    
    fun updateLecture(lecture: Lecture) {
        viewModelScope.launch {
            Log.d(TAG, "Updating lecture: ${lecture.title}")
            repository.updateLecture(lecture)
            
            // Reschedule or cancel reminder based on reminder enabled status
            if (lecture.reminderEnabled) {
                Log.d(TAG, "Rescheduling reminder for lecture: ${lecture.title}")
                ReminderScheduler.scheduleLectureReminder(context, lecture)
            } else {
                Log.d(TAG, "Cancelling reminder for lecture: ${lecture.title}")
                ReminderScheduler.cancelLectureReminder(context, lecture.id)
            }
        }
    }
    
    fun deleteLecture(lecture: Lecture) {
        viewModelScope.launch {
            Log.d(TAG, "Deleting lecture: ${lecture.title}")
            repository.deleteLecture(lecture)
            
            // Cancel the reminder
            Log.d(TAG, "Cancelling reminder for deleted lecture: ${lecture.title}")
            ReminderScheduler.cancelLectureReminder(context, lecture.id)
        }
    }

}
