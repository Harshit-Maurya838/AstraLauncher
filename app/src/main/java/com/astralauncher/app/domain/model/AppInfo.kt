package com.astralauncher.app.domain.model

import android.content.Intent
import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val launchIntent: Intent?
)
