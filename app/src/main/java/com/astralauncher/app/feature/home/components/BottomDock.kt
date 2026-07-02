package com.astralauncher.app.feature.home.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun BottomDock(
    onDrawerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                shape = RoundedCornerShape(32.dp)
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DockIcon(
            icon = Icons.Default.Call,
            onClick = { launchIntent(context, Intent.ACTION_DIAL) }
        )
        DockIcon(
            icon = Icons.Default.Message,
            onClick = { launchIntent(context, Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MESSAGING) }
        )
        DockIcon(
            icon = Icons.Default.Apps,
            onClick = onDrawerClick,
            isDrawer = true
        )
        DockIcon(
            icon = Icons.Default.Language,
            onClick = { launchIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))) }
        )
        DockIcon(
            icon = Icons.Default.CameraAlt,
            onClick = { launchIntent(context, MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA) }
        )
    }
}

@Composable
private fun DockIcon(
    icon: ImageVector,
    onClick: () -> Unit,
    isDrawer: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                if (isDrawer) MaterialTheme.colorScheme.primaryContainer 
                else MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDrawer) MaterialTheme.colorScheme.onPrimaryContainer 
                   else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun launchIntent(context: Context, action: String): Intent {
    val intent = Intent(action)
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return intent
}

private fun launchIntent(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
