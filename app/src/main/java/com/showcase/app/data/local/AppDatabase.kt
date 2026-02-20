package com.showcase.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.showcase.app.data.local.dao.TaskDao
import com.showcase.app.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
