package com.astralauncher.app.core.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Drawer : Screen("drawer")
    object Settings : Screen("settings")
    object Tasks : Screen("tasks")
    object Notes : Screen("notes")
    object NoteEditor : Screen("note_editor/{noteId}") {
        fun createRoute(noteId: Long) = "note_editor/$noteId"
    }
}
