package com.astralauncher.app.feature.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.astralauncher.app.feature.tasks.TaskItem
import com.astralauncher.app.feature.notes.NoteCard
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pinnedTasks by viewModel.pinnedTasks.collectAsState()
    val pinnedNotes by viewModel.pinnedNotes.collectAsState()
    val upcomingEvents by viewModel.upcomingEvents.collectAsState()
    val context = LocalContext.current
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onCalendarPermissionResult(isGranted)
    }

    LaunchedEffect(uiState.calendarPermissionGranted) {
        if (!uiState.calendarPermissionGranted) {
            permissionLauncher.launch(android.Manifest.permission.READ_CALENDAR)
        }
    }

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

            // Upcoming Events
            if (uiState.calendarPermissionGranted && upcomingEvents.isNotEmpty()) {
                val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
                SectionHeader(title = "Upcoming Events")
                Spacer(modifier = Modifier.height(Spacing.medium))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
                    contentPadding = PaddingValues(end = Spacing.large)
                ) {
                    items(upcomingEvents) { event ->
                        MinimalCard(modifier = Modifier.width(200.dp)) {
                            Column(modifier = Modifier.padding(Spacing.medium)) {
                                Text(event.title, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                                Text(timeFormat.format(Date(event.startTimeMillis)), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.large))
            }

            // Quick Actions & Productivity
            SectionHeader(title = "Productivity")
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
            ) {
                MinimalCard(modifier = Modifier.weight(1f).height(80.dp).clickable { navController.navigate("notes") }) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Notes (${pinnedNotes.size})", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                MinimalCard(modifier = Modifier.weight(1f).height(80.dp).clickable { navController.navigate("tasks") }) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tasks (${pinnedTasks.size})", style = MaterialTheme.typography.bodyLarge)
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
