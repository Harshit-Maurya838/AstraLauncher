package com.astralauncher.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.astralauncher.app.domain.model.LauncherSettings
import com.astralauncher.app.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val ACCENT_COLOR_HEX = stringPreferencesKey("accent_color_hex")
    }

    override val settingsFlow: Flow<LauncherSettings> = context.dataStore.data.map { preferences ->
        LauncherSettings(
            isDarkMode = preferences[IS_DARK_MODE] ?: false,
            accentColorHex = preferences[ACCENT_COLOR_HEX]
        )
    }

    override suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = enabled
        }
    }

    override suspend fun setAccentColor(hex: String?) {
        context.dataStore.edit { preferences ->
            if (hex != null) {
                preferences[ACCENT_COLOR_HEX] = hex
            } else {
                preferences.remove(ACCENT_COLOR_HEX)
            }
        }
    }
}
