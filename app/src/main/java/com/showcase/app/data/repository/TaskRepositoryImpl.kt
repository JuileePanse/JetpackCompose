package com.showcase.app.data.repository

import com.showcase.app.data.local.dao.TaskDao
import com.showcase.app.data.local.entity.TaskEntity
import com.showcase.app.domain.model.Task
import com.showcase.app.domain.model.TaskPriority
import com.showcase.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Single source of truth for task data. Abstracts local (Room) data source.
 * Could be extended with remote API and cache-first strategy.
 */
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun observeAllTasks(): Flow<List<Task>> =
        taskDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeSearch(query: String): Flow<List<Task>> =
        taskDao.observeSearch(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getTaskById(id: Long): Task? =
        taskDao.getById(id)?.toDomain()

    override suspend fun addTask(title: String, description: String, priority: TaskPriority): Long {
        val now = System.currentTimeMillis()
        val entity = TaskEntity(
            title = title,
            description = description,
            priority = priority.value,
            isCompleted = false,
            createdAtMillis = now,
            updatedAtMillis = now
        )
        return taskDao.insert(entity)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.update(task.toEntity())
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteById(id)
    }

    override suspend fun toggleComplete(id: Long) {
        taskDao.getById(id)?.let { entity ->
            taskDao.update(
                entity.copy(
                    isCompleted = !entity.isCompleted,
                    updatedAtMillis = System.currentTimeMillis()
                )
            )
        }
    }
}

private fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    priority = TaskPriority.fromValue(priority),
    isCompleted = isCompleted,
    createdAtMillis = createdAtMillis,
    updatedAtMillis = updatedAtMillis
)

private fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    priority = priority.value,
    isCompleted = isCompleted,
    createdAtMillis = createdAtMillis,
    updatedAtMillis = updatedAtMillis
)
