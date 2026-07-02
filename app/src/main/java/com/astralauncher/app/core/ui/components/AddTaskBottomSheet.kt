package com.astralauncher.app.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astralauncher.app.core.theme.Spacing
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    onSave: (title: String, priority: Int) -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskPriority by remember { mutableStateOf(0) }
    
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.large)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "New Task",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(Spacing.large))
            
            BasicTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                decorationBox = { innerTextField ->
                    if (taskTitle.isEmpty()) {
                        Text(
                            text = "What do you need to do?",
                            style = TextStyle(fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(Spacing.extraLarge))
            
            Text(
                text = "Priority",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.small)) {
                PriorityAnimatedChip(label = "Low", selected = taskPriority == 0) { taskPriority = 0 }
                PriorityAnimatedChip(label = "Medium", selected = taskPriority == 1) { taskPriority = 1 }
                PriorityAnimatedChip(label = "High", selected = taskPriority == 2) { taskPriority = 2 }
            }
            
            Spacer(modifier = Modifier.height(Spacing.extraLarge))
            
            Button(
                onClick = { if (taskTitle.isNotBlank()) onSave(taskTitle, taskPriority) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape
            ) {
                Text(
                    text = "Save Task",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun PriorityAnimatedChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val containerColor = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurfaceVariant
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.medium, vertical = Spacing.small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}
