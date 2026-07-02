package com.astralauncher.app.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.astralauncher.app.core.theme.Spacing
import java.util.Locale

@Composable
fun DateWidget(
    dateString: String,
    modifier: Modifier = Modifier
) {
    // We expect dateString to be something like "Wednesday, Jul 12"
    // Or whatever format HomeViewModel emits
    val parts = dateString.split(",")
    val dayOfWeek = if (parts.isNotEmpty()) parts[0].trim() else ""
    val monthDate = if (parts.size > 1) parts[1].trim().uppercase(Locale.getDefault()) else ""
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (monthDate.isNotEmpty()) {
            Text(
                text = monthDate,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(Spacing.small))
        }
        
        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}
