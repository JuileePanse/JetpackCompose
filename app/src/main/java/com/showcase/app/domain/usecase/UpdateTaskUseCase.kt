package com.showcase.app.domain.usecase

import com.showcase.app.domain.model.Task
import com.showcase.app.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
}
