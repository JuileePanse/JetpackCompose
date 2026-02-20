package com.showcase.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a task in local persistence.
 * Keeps data layer representation separate from domain/UI models.
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val priority: Int,
    val isCompleted: Boolean,
    val createdAtMillis: Long,
    val updatedAtMillis: Long
)
