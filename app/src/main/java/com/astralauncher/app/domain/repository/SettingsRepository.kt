package com.astralauncher.app.domain.repository

import com.astralauncher.app.domain.model.LauncherSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<LauncherSettings>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setAccentColor(hex: String?)
}
