package com.astralauncher.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astralauncher.app.domain.model.AppInfo
import com.astralauncher.app.domain.model.CalendarEvent
import com.astralauncher.app.domain.model.Note
import com.astralauncher.app.domain.model.Task
import com.astralauncher.app.domain.repository.CalendarRepository
import com.astralauncher.app.domain.repository.NoteRepository
import com.astralauncher.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class HomeUiState(
    val greeting: String = "",
    val time: String = "",
    val date: String = "",
    val calendarPermissionGranted: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val noteRepository: NoteRepository,
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val pinnedTasks: StateFlow<List<Task>> = taskRepository.getPinnedTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pinnedNotes: StateFlow<List<Note>> = noteRepository.getPinnedNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val upcomingEvents: StateFlow<List<CalendarEvent>> = calendarRepository.getUpcomingEvents(5)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        startClock()
        checkCalendarPermission()
    }

    private fun checkCalendarPermission() {
        val granted = calendarRepository.checkCalendarPermission()
        _uiState.value = _uiState.value.copy(calendarPermissionGranted = granted)
    }

    fun onCalendarPermissionResult(granted: Boolean) {
        _uiState.value = _uiState.value.copy(calendarPermissionGranted = granted)
        // If granted, the flow for upcomingEvents might need to be restarted, 
        // but since we collect it in compose, it will recompose. However the repository flow 
        // only emitted once initially. A better approach is to expose a refresh trigger.
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                updateTimeAndDate()
                delay(1000) // Update every second
            }
        }
    }

    private fun updateTimeAndDate() {
        val now = Date()
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        val hour = SimpleDateFormat("HH", Locale.getDefault()).format(now).toInt()
        
        val greeting = when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }

        _uiState.value = _uiState.value.copy(
            time = timeFormat.format(now),
            date = dateFormat.format(now),
            greeting = greeting
        )
    }
}
