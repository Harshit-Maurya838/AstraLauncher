package com.astralauncher.app.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.astralauncher.app.core.theme.Spacing

@Composable
fun LargeClock(
    timeString: String,
    modifier: Modifier = Modifier
) {
    // Time format is expected to be "HH:mm" or "h:mm a"
    // Let's extract the numbers
    val parts = timeString.split(":")
    if (parts.size >= 2) {
        val hours = parts[0]
        val minutes = parts[1].take(2) // in case it has AM/PM
        
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Outlined Hours
            Text(
                text = hours,
                style = MaterialTheme.typography.displayLarge.copy(
                    drawStyle = Stroke(
                        miter = 10f,
                        width = 4f
                    )
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.width(Spacing.small))
            
            // Bold Minutes
            Text(
                text = minutes,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        // Fallback
        Text(
            text = timeString,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
        )
    }
}
