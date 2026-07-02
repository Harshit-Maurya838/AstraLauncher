package com.astralauncher.app.core.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.astralauncher.app.core.theme.AppDimensions
import com.astralauncher.app.core.theme.Black
import com.astralauncher.app.core.theme.White

@Composable
fun FloatingDock(
    onPhoneClick: () -> Unit,
    onMessageClick: () -> Unit,
    onBrowserClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDrawerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .fillMaxWidth()
            .height(AppDimensions.dockHeight)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(AppDimensions.cornerRadius),
                ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(AppDimensions.cornerRadius),
        // Translucent background for "blur" effect
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DockAction(icon = Icons.Outlined.Phone, onClick = onPhoneClick)
            DockAction(icon = Icons.Outlined.Message, onClick = onMessageClick)
            
            // Center Add Button (Drawer for now, or animated Add)
            CenterDockAction(onClick = onDrawerClick)
            
            DockAction(icon = Icons.Outlined.Language, onClick = onBrowserClick)
            DockAction(icon = Icons.Outlined.CameraAlt, onClick = onCameraClick)
        }
    }
}

@Composable
fun DockAction(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale_anim"
    )

    Box(
        modifier = modifier
            .size(48.dp)
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Custom indication handled by scale
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(AppDimensions.dockIconSize),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CenterDockAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale_anim"
    )

    Box(
        modifier = modifier
            .size(56.dp)
            .scale(scale)
            .shadow(4.dp, CircleShape)
            .background(Black, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Drawer",
            modifier = Modifier.size(32.dp),
            tint = White
        )
    }
}
