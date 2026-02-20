package com.showcase.app.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showcase.app.domain.model.Task
import com.showcase.app.domain.model.TaskPriority
import com.showcase.app.domain.usecase.DeleteTaskUseCase
import com.showcase.app.domain.usecase.GetTaskByIdUseCase
import com.showcase.app.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskDetailUiState(
    val task: Task? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val taskId: Long = checkNotNull(savedStateHandle["taskId"])

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val task = getTaskByIdUseCase(taskId)
            _uiState.update {
                it.copy(task = task, isLoading = false, error = if (task == null) "Task not found" else null)
            }
        }
    }

    fun updateTask(
        title: String,
        description: String,
        priority: TaskPriority,
        isCompleted: Boolean
    ) {
        val current = _uiState.value.task ?: return
        viewModelScope.launch {
            updateTaskUseCase(
                current.copy(
                    title = title,
                    description = description,
                    priority = priority,
                    isCompleted = isCompleted,
                    updatedAtMillis = System.currentTimeMillis()
                )
            )
            loadTask()
        }
    }

    fun toggleComplete() {
        val task = _uiState.value.task ?: return
        viewModelScope.launch {
            updateTaskUseCase(task.copy(isCompleted = !task.isCompleted, updatedAtMillis = System.currentTimeMillis()))
            loadTask()
        }
    }

    fun deleteTask(onDeleted: () -> Unit) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
            onDeleted()
        }
    }
}
