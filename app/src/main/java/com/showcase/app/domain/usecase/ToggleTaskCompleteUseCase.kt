package com.showcase.app.domain.usecase

import com.showcase.app.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskCompleteUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long) = repository.toggleComplete(id)
}
