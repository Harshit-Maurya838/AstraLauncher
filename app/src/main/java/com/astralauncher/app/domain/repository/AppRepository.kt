package com.astralauncher.app.domain.repository

import com.astralauncher.app.domain.model.AppInfo
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun getInstalledApps(): List<AppInfo>
    
    // Using Flow so we can later react to package installs/uninstalls
    fun getInstalledAppsFlow(): Flow<List<AppInfo>>
}
