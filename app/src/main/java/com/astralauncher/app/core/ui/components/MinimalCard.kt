package com.astralauncher.app.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.astralauncher.app.core.theme.AppDimensions
import com.astralauncher.app.core.theme.Spacing

@Composable
fun MinimalCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(AppDimensions.cornerRadius),
                ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(AppDimensions.cornerRadius),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(modifier = Modifier.padding(Spacing.defaultPadding)) {
            content()
        }
    }
}
