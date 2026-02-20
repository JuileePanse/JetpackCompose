package com.showcase.app.domain.usecase

import com.showcase.app.domain.model.Task
import com.showcase.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<Task>> = repository.observeSearch(query)
}
