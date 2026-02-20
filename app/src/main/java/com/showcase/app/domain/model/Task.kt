package com.showcase.app.domain.model

/**
 * Domain model for a Task. UI and business logic use this;
 * data layer maps to/from TaskEntity.
 */
data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val priority: TaskPriority,
    val isCompleted: Boolean,
    val createdAtMillis: Long,
    val updatedAtMillis: Long
)

enum class TaskPriority(val value: Int, val label: String) {
    LOW(0, "Low"),
    MEDIUM(1, "Medium"),
    HIGH(2, "High");

    companion object {
        fun fromValue(value: Int): TaskPriority = entries.find { it.value == value } ?: MEDIUM
    }
}
