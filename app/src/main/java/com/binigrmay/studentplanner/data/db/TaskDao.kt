package com.binigrmay.studentplanner.data.db

import androidx.room.*
import com.binigrmay.studentplanner.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Task entity
 */
@Dao
interface TaskDao {
    
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getIncompleteTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY dueDate DESC")
    fun getCompletedTasks(): Flow<List<Task>>
    
    @Query("""
        SELECT * FROM tasks 
        WHERE dueDate >= :startOfDay 
        AND dueDate < :endOfDay 
        ORDER BY dueDate ASC
    """)
    fun getTasksForDay(startOfDay: Long, endOfDay: Long): Flow<List<Task>>
    
    @Query("""
        SELECT * FROM tasks 
        WHERE dueDate >= :startOfWeek 
        AND dueDate < :endOfWeek 
        ORDER BY dueDate ASC
    """)
    fun getTasksForWeek(startOfWeek: Long, endOfWeek: Long): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY dueDate ASC")
    fun getTasksByPriority(priority: String): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY dueDate ASC")
    fun getTasksByCategory(category: String): Flow<List<Task>>
    
    @Query("""
        SELECT * FROM tasks 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY dueDate ASC
    """)
    fun searchTasks(query: String): Flow<List<Task>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)
    
    @Update
    suspend fun updateTask(task: Task)
    
    @Delete
    suspend fun deleteTask(task: Task)
    
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)
    
    @Query("DELETE FROM tasks WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()
    
    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)
}
