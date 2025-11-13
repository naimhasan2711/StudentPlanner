package com.binigrmay.studentplanner.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binigrmay.studentplanner.data.model.Task
import com.binigrmay.studentplanner.data.repository.TaskRepository
import com.binigrmay.studentplanner.utils.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for managing Task-related UI state and operations
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    companion object {
        private const val TAG = "TaskViewModel"
    }
    
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

    // Search query
    private val _searchQuery = MutableStateFlow("")

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
            Log.d(TAG, "Inserting task: ${task.title}")
            val taskId = repository.insertTask(task)
            Log.d(TAG, "Task inserted with ID: $taskId")
            
            // Schedule reminder if enabled
            if (task.reminderEnabled && taskId > 0) {
                val taskWithId = task.copy(id = taskId.toInt())
                Log.d(TAG, "Scheduling reminder for task: ${taskWithId.title}")
                ReminderScheduler.scheduleTaskReminder(context, taskWithId)
            }
        }
    }
    
    fun updateTask(task: Task) {
        viewModelScope.launch {
            Log.d(TAG, "Updating task: ${task.title}")
            repository.updateTask(task)
            
            // Reschedule or cancel reminder based on reminder enabled status
            if (task.reminderEnabled) {
                Log.d(TAG, "Rescheduling reminder for task: ${task.title}")
                ReminderScheduler.scheduleTaskReminder(context, task)
            } else {
                Log.d(TAG, "Cancelling reminder for task: ${task.title}")
                ReminderScheduler.cancelTaskReminder(context, task.id)
            }
        }
    }
    
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            Log.d(TAG, "Deleting task: ${task.title}")
            repository.deleteTask(task)
            
            // Cancel the reminder
            Log.d(TAG, "Cancelling reminder for deleted task: ${task.title}")
            ReminderScheduler.cancelTaskReminder(context, task.id)
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

}
