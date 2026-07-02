package com.astralauncher.app.feature.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astralauncher.app.core.navigation.Screen
import com.astralauncher.app.core.theme.Spacing
import com.astralauncher.app.core.ui.components.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            FloatingDock(
                onPhoneClick = { launchIntent(context, Intent.ACTION_DIAL) },
                onMessageClick = { launchIntent(context, Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MESSAGING) },
                onBrowserClick = { launchIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))) },
                onCameraClick = { launchIntent(context, MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA) },
                onDrawerClick = { navController.navigate(Screen.Drawer.route) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.large, vertical = Spacing.extraLarge),
            horizontalAlignment = Alignment.Start
        ) {
            
            // Top Section: Clock and Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = uiState.greeting,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(Spacing.extraSmall))
                    LargeClock(timeString = uiState.time)
                    Spacer(modifier = Modifier.height(Spacing.extraSmall))
                    DateWidget(dateString = uiState.date)
                }
                
                IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.extraLarge))

            // Search Bar
            SearchBar(
                onClick = { navController.navigate(Screen.Drawer.route) }
            )

            Spacer(modifier = Modifier.height(Spacing.extraLarge))

            // Quick Actions / Pinned
            SectionHeader(title = "Pinned")
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
            ) {
                MinimalCard(modifier = Modifier.weight(1f).height(100.dp)) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Notes", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                MinimalCard(modifier = Modifier.weight(1f).height(100.dp)) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tasks", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

private fun launchIntent(context: Context, action: String): Intent {
    val intent = Intent(action)
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return intent
}

private fun launchIntent(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
