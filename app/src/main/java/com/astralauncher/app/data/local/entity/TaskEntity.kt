package com.astralauncher.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val isPinned: Boolean = false,
    val priority: Int = 0, // 0: Low, 1: Medium, 2: High
    val dueDateMillis: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
