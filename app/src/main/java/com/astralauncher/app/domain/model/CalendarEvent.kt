package com.astralauncher.app.domain.model

data class CalendarEvent(
    val id: Long,
    val title: String,
    val description: String?,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val location: String?,
    val isAllDay: Boolean
)
