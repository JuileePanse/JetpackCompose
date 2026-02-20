package com.showcase.app.domain.usecase

import com.showcase.app.domain.model.Task
import com.showcase.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.observeAllTasks()
}
