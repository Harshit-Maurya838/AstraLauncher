package com.astralauncher.app.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val isPinned: Boolean = false,
    val priority: Int = 0, // 0: Low, 1: Medium, 2: High
    val dueDateMillis: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
