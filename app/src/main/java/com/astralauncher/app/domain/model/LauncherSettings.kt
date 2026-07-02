package com.astralauncher.app.domain.model

data class LauncherSettings(
    val isDarkMode: Boolean = false,
    val accentColorHex: String? = null // null means system default
)
