package com.astralauncher.app.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.astralauncher.app.core.theme.Spacing
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateWidget(
    dateString: String, // Kept for backwards compatibility if needed, but we'll compute it
    modifier: Modifier = Modifier
) {
    val calendar = remember { Calendar.getInstance() }
    val monthFormat = remember { SimpleDateFormat("MMM", Locale.getDefault()) }
    
    val currentMonth = monthFormat.format(calendar.time).uppercase(Locale.getDefault())
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentMonth,
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.width(Spacing.medium))
        
        Text(
            text = "•",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.width(Spacing.medium))
        
        Text(
            text = "$currentDay / $totalDays",
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}
