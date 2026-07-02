package com.astralauncher.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.astralauncher.app.feature.drawer.AppDrawerScreen
import com.astralauncher.app.feature.home.HomeScreen
import com.astralauncher.app.feature.settings.SettingsScreen
import com.astralauncher.app.feature.tasks.TasksScreen
import com.astralauncher.app.feature.notes.NotesScreen
import com.astralauncher.app.feature.notes.NoteEditorScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun AstraNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Drawer.route) {
            AppDrawerScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        
        composable(Screen.Tasks.route) {
            TasksScreen(navController = navController)
        }
        
        composable(Screen.Notes.route) {
            NotesScreen(navController = navController)
        }
        
        composable(
            route = Screen.NoteEditor.route,
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
            NoteEditorScreen(navController = navController, noteId = noteId)
        }
    }
}
