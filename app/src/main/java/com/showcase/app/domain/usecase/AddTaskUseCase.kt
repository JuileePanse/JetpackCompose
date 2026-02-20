package com.showcase.app.domain.usecase

import com.showcase.app.domain.model.TaskPriority
import com.showcase.app.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        priority: TaskPriority = TaskPriority.MEDIUM
    ): Long = repository.addTask(title, description, priority)
}
