package com.astralauncher.app.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.astralauncher.app.domain.model.AppInfo
import com.astralauncher.app.domain.repository.AppRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : AppRepository {

    private val packageManager: PackageManager = context.packageManager

    override suspend fun getInstalledApps(): List<AppInfo> = withContext(ioDispatcher) {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
        
        resolveInfoList.mapNotNull { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            val label = resolveInfo.loadLabel(packageManager).toString()
            val icon = resolveInfo.loadIcon(packageManager)
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            
            if (packageName != context.packageName) { // Exclude self
                AppInfo(
                    packageName = packageName,
                    label = label,
                    icon = icon,
                    launchIntent = launchIntent
                )
            } else {
                null
            }
        }.sortedBy { it.label.lowercase() }
    }

    override fun getInstalledAppsFlow(): Flow<List<AppInfo>> = flow {
        // Initial load
        emit(getInstalledApps())
        // In a complete implementation, this would register a BroadcastReceiver 
        // for PACKAGE_ADDED/PACKAGE_REMOVED intents and re-emit.
    }.flowOn(ioDispatcher)
}
