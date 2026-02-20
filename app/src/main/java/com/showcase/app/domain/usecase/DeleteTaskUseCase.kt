package com.showcase.app.domain.usecase

import com.showcase.app.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteTask(id)
}
