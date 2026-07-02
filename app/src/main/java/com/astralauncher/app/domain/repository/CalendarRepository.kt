package com.astralauncher.app.domain.repository

import com.astralauncher.app.domain.model.CalendarEvent
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    fun getUpcomingEvents(limit: Int = 5): Flow<List<CalendarEvent>>
    fun checkCalendarPermission(): Boolean
}
