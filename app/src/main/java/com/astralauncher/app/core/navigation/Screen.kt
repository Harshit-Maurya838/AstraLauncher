package com.astralauncher.app.core.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Drawer : Screen("drawer_screen")
    object Settings : Screen("settings_screen")
}
