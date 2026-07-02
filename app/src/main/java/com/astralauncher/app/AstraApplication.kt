package com.astralauncher.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AstraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any background workers or logging here
    }
}
