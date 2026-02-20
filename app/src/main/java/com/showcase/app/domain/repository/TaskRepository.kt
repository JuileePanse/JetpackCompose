package com.showcase.app.domain.repository

import com.showcase.app.domain.model.Task
import com.showcase.app.domain.model.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeAllTasks(): Flow<List<Task>>
    fun observeSearch(query: String): Flow<List<Task>>
    suspend fun getTaskById(id: Long): Task?
    suspend fun addTask(title: String, description: String, priority: TaskPriority): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Long)
    suspend fun toggleComplete(id: Long)
}
