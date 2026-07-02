package com.astralauncher.app.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.astralauncher.app.domain.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskCard(
    task: Task,
    onComplete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var isChecking by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val priorityColor = when (task.priority) {
        0 -> Color.Gray
        1 -> Color(0xFF2196F3) // Blue
        else -> Color(0xFFE53935) // Red
    }

    val dueDateString = task.dueDateMillis?.let {
        SimpleDateFormat("MMM d h:mm", Locale.getDefault()).format(Date(it))
    } ?: ""

    val relativeTimeString = getRelativeTime(task.dueDateMillis)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (task.dueDateMillis != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "$relativeTimeString • $dueDateString",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right side: Priority Dot + Check Circle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(priorityColor)
                )

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .clickable(enabled = !isChecking) {
                            isChecking = true
                            coroutineScope.launch {
                                delay(300) // Allow animation to play
                                onComplete(task)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !isChecking,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = "Complete",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = isChecking,
                        enter = fadeIn() + scaleIn(animationSpec = tween(200)),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Completed",
                            tint = priorityColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getRelativeTime(dueDateMillis: Long?): String {
    if (dueDateMillis == null) return ""
    val now = System.currentTimeMillis()
    val diff = dueDateMillis - now
    if (diff < 0) return "Overdue"
    
    val minutes = diff / (60 * 1000)
    val hours = minutes / 60
    val days = hours / 24
    
    return when {
        days > 0 -> "${days}d"
        hours > 0 -> "${hours}h"
        minutes > 0 -> "${minutes}m"
        else -> "Now"
    }
}
