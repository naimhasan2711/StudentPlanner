package com.binigrmay.studentplanner.data.repository

import com.binigrmay.studentplanner.data.db.TaskDao
import com.binigrmay.studentplanner.data.model.Priority
import com.binigrmay.studentplanner.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Task data operations
 * Provides a clean API for data access to the rest of the app
 */
@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    
    fun getIncompleteTasks(): Flow<List<Task>> = taskDao.getIncompleteTasks()
    
    fun getCompletedTasks(): Flow<List<Task>> = taskDao.getCompletedTasks()
    
    fun getTasksForDay(startOfDay: Long, endOfDay: Long): Flow<List<Task>> =
        taskDao.getTasksForDay(startOfDay, endOfDay)
    
    fun getTasksForWeek(startOfWeek: Long, endOfWeek: Long): Flow<List<Task>> =
        taskDao.getTasksForWeek(startOfWeek, endOfWeek)
    
    fun getTasksByPriority(priority: Priority): Flow<List<Task>> =
        taskDao.getTasksByPriority(priority.name)
    
    fun getTasksByCategory(category: String): Flow<List<Task>> =
        taskDao.getTasksByCategory(category)
    
    fun searchTasks(query: String): Flow<List<Task>> =
        taskDao.searchTasks(query)
    
    suspend fun getTaskById(taskId: Int): Task? =
        taskDao.getTaskById(taskId)
    
    suspend fun insertTask(task: Task): Long =
        taskDao.insertTask(task)
    
    suspend fun insertTasks(tasks: List<Task>) =
        taskDao.insertTasks(tasks)
    
    suspend fun updateTask(task: Task) =
        taskDao.updateTask(task)
    
    suspend fun deleteTask(task: Task) =
        taskDao.deleteTask(task)
    
    suspend fun deleteTaskById(taskId: Int) =
        taskDao.deleteTaskById(taskId)
    
    suspend fun deleteCompletedTasks() =
        taskDao.deleteCompletedTasks()
    
    suspend fun toggleTaskCompletion(taskId: Int, isCompleted: Boolean) =
        taskDao.updateTaskCompletion(taskId, isCompleted)
}
