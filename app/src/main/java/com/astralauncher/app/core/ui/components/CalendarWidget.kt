package com.astralauncher.app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astralauncher.app.core.theme.RefActiveDot
import com.astralauncher.app.core.theme.RefInactiveDot
import com.astralauncher.app.core.theme.Spacing
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarWidget(modifier: Modifier = Modifier) {
    val calendar = remember { Calendar.getInstance() }
    val monthFormat = remember { SimpleDateFormat("MMM", Locale.getDefault()) }
    
    val currentMonth = monthFormat.format(calendar.time).uppercase(Locale.getDefault())
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Side: Month and Day/Total
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = currentMonth,
                style = MaterialTheme.typography.titleMedium.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "• $currentDay / $totalDays",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        
        Spacer(modifier = Modifier.width(Spacing.medium))
        
        // Right Side: Dot Calendar
        DotCalendar(currentDay = currentDay, totalDays = totalDays)
    }
}

@Composable
fun DotCalendar(currentDay: Int, totalDays: Int) {
    // 7 columns x 5 rows = 35 dots
    val cols = 7
    val rows = 5
    
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (r in 0 until rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (c in 0 until cols) {
                    val dotIndex = r * cols + c + 1
                    // Only show up to totalDays, or keep layout fixed at 35?
                    // "Active day = solid black dot, Remaining dots = light gray"
                    // If it's a grid, we usually just show the actual days of the month.
                    val isActive = dotIndex == currentDay
                    val isVisible = dotIndex <= totalDays
                    
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (!isVisible) androidx.compose.ui.graphics.Color.Transparent
                                else if (isActive) RefActiveDot
                                else RefInactiveDot
                            )
                    )
                }
            }
        }
    }
}
