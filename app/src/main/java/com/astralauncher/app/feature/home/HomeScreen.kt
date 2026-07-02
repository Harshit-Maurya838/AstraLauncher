package com.astralauncher.app.feature.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astralauncher.app.core.navigation.Screen
import com.astralauncher.app.core.theme.Spacing
import com.astralauncher.app.core.ui.components.*
import com.astralauncher.app.feature.search.UnifiedSearchViewModel
import com.astralauncher.app.feature.tasks.TasksViewModel
import com.astralauncher.app.domain.model.AppInfo

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    searchViewModel: UnifiedSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pendingTasks by viewModel.pendingTasks.collectAsStateWithLifecycle()
    val apps by searchViewModel.apps.collectAsStateWithLifecycle()
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    
    val tasksViewModel = androidx.hilt.navigation.compose.hiltViewModel<TasksViewModel>()
    
    val filteredTasks by remember(pendingTasks, searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) pendingTasks
            else pendingTasks.filter { it.title.contains(searchQuery, ignoreCase = true) }
        }
    }
    
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("launcher_prefs", Context.MODE_PRIVATE) }
    
    var isAppsSheetOpen by remember { mutableStateOf(false) }
    var isFavoritePickerOpen by remember { mutableStateOf(false) }
    var isAddTaskSheetOpen by remember { mutableStateOf(false) }
    
    val appsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Manage Favorite Apps
    var favoritePackages by remember { 
        mutableStateOf(sharedPrefs.getStringSet("favorite_apps", emptySet()) ?: emptySet()) 
    }
    
    val favoriteApps = remember(favoritePackages, apps) {
        apps.filter { it.packageName in favoritePackages }
    }
    
    val toggleFavorite = { app: AppInfo ->
        val current = favoritePackages.toMutableSet()
        if (current.contains(app.packageName)) {
            current.remove(app.packageName)
        } else {
            current.clear()
            current.add(app.packageName)
        }
        favoritePackages = current
        sharedPrefs.edit { putStringSet("favorite_apps", current) }
    }
    
    Scaffold(
        containerColor = com.astralauncher.app.core.theme.RefBackground,
        bottomBar = {
            FloatingDock(
                favoriteApps = favoriteApps,
                onFavoritePlaceholderClick = { isFavoritePickerOpen = true },
                onAppClick = { app ->
                    app.launchIntent?.let { launchIntent(context, it) }
                },
                onRemoveFavorite = { app -> toggleFavorite(app) },
                onAddClick = { isAddTaskSheetOpen = true },
                onAppsClick = { isAppsSheetOpen = true },
                modifier = Modifier.padding(bottom = Spacing.medium)
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
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = uiState.greeting,
                        style = MaterialTheme.typography.titleSmall,
                        color = com.astralauncher.app.core.theme.RefSecondaryText
                    )
                    Spacer(modifier = Modifier.height(Spacing.extraSmall))
                    LargeClock(timeString = uiState.time)
                    Spacer(modifier = Modifier.height(Spacing.extraSmall))
                    DateWidget(dateString = uiState.date)
                }
                
                CalendarWidget()
            }

            Spacer(modifier = Modifier.height(Spacing.massive))

            // Search Bar
            SearchBar(
                onClick = { isAppsSheetOpen = true }
            )

            Spacer(modifier = Modifier.height(Spacing.medium))
            
            Text(
                text = "✓ TASKS • ${filteredTasks.size}",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                color = Color.Gray,
                modifier = Modifier.padding(vertical = Spacing.small)
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))

            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No upcoming tasks",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = Spacing.extraLarge),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            onComplete = { tasksViewModel.toggleTaskCompletion(it) },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
        
        if (isAppsSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isAppsSheetOpen = false },
                sheetState = appsSheetState,
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                dragHandle = { BottomSheetDefaults.DragHandle() },
                modifier = Modifier.fillMaxHeight(0.6f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.large)
                ) {
                    // Quick Search Inside Sheet
                    androidx.compose.foundation.text.BasicTextField(
                        value = searchQuery,
                        onValueChange = searchViewModel::onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing.medium)
                            .background(com.astralauncher.app.core.theme.LightGray, CircleShape)
                            .padding(horizontal = Spacing.large, vertical = Spacing.medium),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text("Search Apps...", color = com.astralauncher.app.core.theme.RefSecondaryText)
                            }
                            innerTextField()
                        }
                    )
                    
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = Spacing.medium)
                    ) {
                        items(apps) { app ->
                            com.astralauncher.app.core.ui.components.AppTile(
                                app = app,
                                onClick = {
                                    app.launchIntent?.let { launchIntent(context, it) }
                                }
                            )
                        }
                    }
                    
                    HorizontalDivider(color = com.astralauncher.app.core.theme.LightGray)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isAppsSheetOpen = false
                                navController.navigate(Screen.Settings.route)
                            }
                            .padding(vertical = Spacing.large),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(Spacing.medium))
                        Column {
                            Text("Settings", style = MaterialTheme.typography.titleMedium)
                            Text("Launcher Preferences", style = MaterialTheme.typography.bodyMedium, color = com.astralauncher.app.core.theme.RefSecondaryText)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(Spacing.medium))
                }
            }
        }
        
        if (isFavoritePickerOpen) {
            ModalBottomSheet(
                onDismissRequest = { isFavoritePickerOpen = false },
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                modifier = Modifier.fillMaxHeight(0.6f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.large)
                ) {
                    SectionHeader("Select Favorite App")
                    Spacer(modifier = Modifier.height(Spacing.medium))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = Spacing.large)
                    ) {
                        items(apps) { app ->
                            val isSelected = favoritePackages.contains(app.packageName)
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { toggleFavorite(app) }
                                    .padding(vertical = Spacing.small),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                androidx.compose.foundation.Image(
                                    painter = com.google.accompanist.drawablepainter.rememberDrawablePainter(drawable = app.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.width(Spacing.medium))
                                Text(
                                    text = app.label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (isAddTaskSheetOpen) {
            AddTaskBottomSheet(
                onDismiss = { isAddTaskSheetOpen = false },
                onSave = { title, priority ->
                    tasksViewModel.addTask(title, priority)
                    isAddTaskSheetOpen = false
                }
            )
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
