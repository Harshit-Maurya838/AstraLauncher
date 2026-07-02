package com.astralauncher.app.core.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.astralauncher.app.domain.model.AppInfo
import com.astralauncher.app.core.theme.AppDimensions
import com.astralauncher.app.core.theme.Black
import com.astralauncher.app.core.theme.White

@Composable
fun FloatingDock(
    favoriteApps: List<AppInfo>,
    onFavoritePlaceholderClick: () -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onRemoveFavorite: (AppInfo) -> Unit,
    onAddClick: () -> Unit,
    onAppsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 32.dp, vertical = 32.dp)
            .fillMaxWidth()
            .height(AppDimensions.dockHeight + 8.dp) // Slightly taller
            .shadow(
                elevation = 16.dp, // more shadow
                shape = RoundedCornerShape(32.dp), // More rounded
                ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
                spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(32.dp),
        // Translucent background for "blur" effect
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f), // High opacity
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (favoriteApps.isEmpty()) {
                    DockAction(
                        icon = Icons.Outlined.FavoriteBorder, 
                        onClick = onFavoritePlaceholderClick
                    )
                } else {
                    favoriteApps.take(1).forEach { app ->
                        DockAppAction(
                            app = app,
                            onClick = { onAppClick(app) },
                            onRemove = { onRemoveFavorite(app) }
                        )
                    }
                }
            }
            
            // Center Add Button
            CenterDockAction(onClick = onAddClick)
            
            DockAction(icon = Icons.Outlined.GridView, onClick = onAppsClick)
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun DockAction(
    icon: ImageVector,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
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
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
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
            contentDescription = "Add",
            modifier = Modifier.size(32.dp),
            tint = White
        )
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun DockAppAction(
    app: AppInfo,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var showMenu by remember { mutableStateOf(false) }
    
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
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = { showMenu = true }
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = app.icon),
            contentDescription = app.label,
            modifier = Modifier.size(AppDimensions.dockIconSize)
        )
        
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            offset = DpOffset(x = 0.dp, y = (-100).dp)
        ) {
            DropdownMenuItem(
                text = { Text("Remove from Favorites") },
                onClick = { 
                    showMenu = false
                    onRemove()
                }
            )
            DropdownMenuItem(
                text = { Text("Cancel") },
                onClick = { showMenu = false }
            )
        }
    }
}
