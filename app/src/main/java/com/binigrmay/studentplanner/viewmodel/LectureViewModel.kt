package com.binigrmay.studentplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binigrmay.studentplanner.data.model.DayOfWeek
import com.binigrmay.studentplanner.data.model.Lecture
import com.binigrmay.studentplanner.data.repository.LectureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for managing Lecture-related UI state and operations
 */
@HiltViewModel
class LectureViewModel @Inject constructor(
    private val repository: LectureRepository
) : ViewModel() {
    
    // All lectures
    val allLectures: StateFlow<List<Lecture>> = repository.getAllLectures()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Recurring lectures
    val recurringLectures: StateFlow<List<Lecture>> = repository.getRecurringLectures()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // One-time lectures
    val oneTimeLectures: StateFlow<List<Lecture>> = repository.getOneTimeLectures()
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
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // Search results
    val searchResults: StateFlow<List<Lecture>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                repository.searchLectures(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
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
            repository.insertLecture(lecture)
        }
    }
    
    fun updateLecture(lecture: Lecture) {
        viewModelScope.launch {
            repository.updateLecture(lecture)
        }
    }
    
    fun deleteLecture(lecture: Lecture) {
        viewModelScope.launch {
            repository.deleteLecture(lecture)
        }
    }
    
    fun deleteAllLectures() {
        viewModelScope.launch {
            repository.deleteAllLectures()
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun getLecturesByInstructor(instructorName: String): Flow<List<Lecture>> {
        return repository.getLecturesByInstructor(instructorName)
    }
}
