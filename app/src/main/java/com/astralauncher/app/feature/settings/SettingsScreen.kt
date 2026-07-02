package com.astralauncher.app.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astralauncher.app.core.theme.Spacing
import com.astralauncher.app.core.ui.components.MinimalCard
import com.astralauncher.app.core.ui.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Launcher Settings", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.large)
        ) {
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Theme Category
            SectionHeader(title = "Appearance", modifier = Modifier.padding(bottom = Spacing.medium))
            
            MinimalCard {
                Column {
                    ListItem(
                        headlineContent = { Text("Dark Mode", style = MaterialTheme.typography.bodyLarge) },
                        supportingContent = { Text("Toggle application dark theme", style = MaterialTheme.typography.bodyMedium) },
                        leadingContent = { Icon(Icons.Outlined.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface) },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
                        trailingContent = {
                            Switch(
                                checked = settings.isDarkMode,
                                onCheckedChange = { viewModel.toggleDarkMode(it) }
                            )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = Spacing.medium), color = MaterialTheme.colorScheme.surfaceVariant)

                    ListItem(
                        headlineContent = { Text("Accent Color", style = MaterialTheme.typography.bodyLarge) },
                        supportingContent = { Text(settings.accentColorHex ?: "System Default", style = MaterialTheme.typography.bodyMedium) },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
                        leadingContent = { Icon(Icons.Outlined.ColorLens, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.extraLarge))

            // About Category
            SectionHeader(title = "About", modifier = Modifier.padding(bottom = Spacing.medium))

            MinimalCard {
                ListItem(
                    headlineContent = { Text("Astra Launcher", style = MaterialTheme.typography.bodyLarge) },
                    supportingContent = { Text("Version 1.0.0 (Monochrome Edition)", style = MaterialTheme.typography.bodyMedium) },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
                    leadingContent = { Icon(Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface) }
                )
            }
        }
    }
}
