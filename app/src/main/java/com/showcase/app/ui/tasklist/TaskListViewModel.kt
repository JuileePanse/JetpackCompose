package com.showcase.app.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showcase.app.domain.model.Task
import com.showcase.app.domain.usecase.DeleteTaskUseCase
import com.showcase.app.domain.usecase.GetAllTasksUseCase
import com.showcase.app.domain.usecase.SearchTasksUseCase
import com.showcase.app.domain.usecase.ToggleTaskCompleteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val searchTasksUseCase: SearchTasksUseCase,
    private val toggleTaskCompleteUseCase: ToggleTaskCompleteUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        observeTasks()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        observeTasks()
    }

    private fun observeTasks() {
        observeJob?.cancel()
        val query = _uiState.value.searchQuery
        observeJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            (if (query.isBlank()) getAllTasksUseCase() else searchTasksUseCase(query))
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Unknown error")
                    }
                }
                .collect { tasks ->
                    _uiState.update {
                        it.copy(tasks = tasks, isLoading = false, error = null)
                    }
                }
        }
    }

    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            toggleTaskCompleteUseCase(task.id)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task.id)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
