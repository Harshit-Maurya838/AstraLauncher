package com.astralauncher.app.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.astralauncher.app.core.theme.AppDimensions
import com.astralauncher.app.core.theme.Spacing

@Composable
fun SearchBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Google Search"
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(AppDimensions.searchBarHeight)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = com.astralauncher.app.core.theme.LightGray,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(Spacing.medium))
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}
