package com.astralauncher.app.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import com.astralauncher.app.domain.model.CalendarEvent
import com.astralauncher.app.domain.repository.CalendarRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CalendarRepository {

    override fun checkCalendarPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun getUpcomingEvents(limit: Int): Flow<List<CalendarEvent>> = flow {
        if (!checkCalendarPermission()) {
            emit(emptyList())
            return@flow
        }

        val events = mutableListOf<CalendarEvent>()
        val contentResolver = context.contentResolver
        
        val now = System.currentTimeMillis()
        val selection = "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DELETED} != 1"
        val selectionArgs = arrayOf(now.toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.ALL_DAY
        )

        try {
            contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idIndex = cursor.getColumnIndexOrThrow(CalendarContract.Events._ID)
                val titleIndex = cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
                val descIndex = cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)
                val startIndex = cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
                val endIndex = cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)
                val locIndex = cursor.getColumnIndexOrThrow(CalendarContract.Events.EVENT_LOCATION)
                val allDayIndex = cursor.getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY)

                while (cursor.moveToNext() && events.size < limit) {
                    val id = cursor.getLong(idIndex)
                    val title = cursor.getString(titleIndex) ?: "No Title"
                    val description = cursor.getString(descIndex)
                    val startTime = cursor.getLong(startIndex)
                    val endTime = cursor.getLong(endIndex)
                    val location = cursor.getString(locIndex)
                    val isAllDay = cursor.getInt(allDayIndex) == 1

                    events.add(
                        CalendarEvent(
                            id = id,
                            title = title,
                            description = description,
                            startTimeMillis = startTime,
                            endTimeMillis = endTime,
                            location = location,
                            isAllDay = isAllDay
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        emit(events)
    }.flowOn(Dispatchers.IO)
}
