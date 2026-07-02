package com.astralauncher.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.astralauncher.app.feature.drawer.AppDrawerScreen
import com.astralauncher.app.feature.home.HomeScreen
import com.astralauncher.app.feature.settings.SettingsScreen

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
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}
