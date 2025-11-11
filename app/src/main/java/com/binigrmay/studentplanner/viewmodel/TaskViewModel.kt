package com.binigrmay.studentplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binigrmay.studentplanner.data.model.Priority
import com.binigrmay.studentplanner.data.model.Task
import com.binigrmay.studentplanner.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for managing Task-related UI state and operations
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    
    // All tasks
    val allTasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Incomplete tasks
    val incompleteTasks: StateFlow<List<Task>> = repository.getIncompleteTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Completed tasks
    val completedTasks: StateFlow<List<Task>> = repository.getCompletedTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Today's tasks
    private val _todaysTasks = MutableStateFlow<List<Task>>(emptyList())
    val todaysTasks: StateFlow<List<Task>> = _todaysTasks.asStateFlow()
    
    // This week's tasks
    private val _thisWeekTasks = MutableStateFlow<List<Task>>(emptyList())
    val thisWeekTasks: StateFlow<List<Task>> = _thisWeekTasks.asStateFlow()
    
    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // Search results
    val searchResults: StateFlow<List<Task>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                repository.searchTasks(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    init {
        loadTodaysTasks()
        loadThisWeekTasks()
    }
    
    private fun loadTodaysTasks() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis
            
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val endOfDay = calendar.timeInMillis
            
            repository.getTasksForDay(startOfDay, endOfDay)
                .collect { tasks ->
                    _todaysTasks.value = tasks
                }
        }
    }
    
    private fun loadThisWeekTasks() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfWeek = calendar.timeInMillis
            
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            val endOfWeek = calendar.timeInMillis
            
            repository.getTasksForWeek(startOfWeek, endOfWeek)
                .collect { tasks ->
                    _thisWeekTasks.value = tasks
                }
        }
    }
    
    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }
    
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
    
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
    
    fun toggleTaskCompletion(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(taskId, isCompleted)
        }
    }
    
    fun deleteCompletedTasks() {
        viewModelScope.launch {
            repository.deleteCompletedTasks()
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun getTasksByPriority(priority: Priority): Flow<List<Task>> {
        return repository.getTasksByPriority(priority)
    }
    
    fun getTasksByCategory(category: String): Flow<List<Task>> {
        return repository.getTasksByCategory(category)
    }
}
