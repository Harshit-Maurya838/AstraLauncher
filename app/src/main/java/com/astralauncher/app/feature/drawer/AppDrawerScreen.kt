package com.astralauncher.app.feature.drawer

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astralauncher.app.core.theme.AppDimensions
import com.astralauncher.app.core.theme.Spacing
import com.astralauncher.app.core.ui.components.AppTile
import com.astralauncher.app.core.ui.components.SectionHeader
import com.astralauncher.app.feature.notes.NoteCard
import com.astralauncher.app.feature.search.UnifiedSearchViewModel
import com.astralauncher.app.feature.tasks.TaskItem
import com.astralauncher.app.domain.model.AppInfo

@Composable
fun AppDrawerScreen(
    navController: NavController,
    viewModel: UnifiedSearchViewModel = hiltViewModel()
) {
    val apps by viewModel.apps.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.large)
        ) {
            Spacer(modifier = Modifier.height(Spacing.extraLarge))
            
            // Custom Search Bar for Drawer
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppDimensions.searchBarHeight),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
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
                    
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search Apps, Tasks, Notes...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = viewModel::onSearchQueryChange,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Spacing.medium))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = Spacing.large),
                verticalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                if (searchQuery.isNotEmpty()) {
                    if (tasks.isNotEmpty()) {
                        item { SectionHeader("Tasks", modifier = Modifier.padding(vertical = Spacing.small)) }
                        items(tasks, key = { "task_${it.id}" }) { task ->
                            TaskItem(
                                task = task,
                                onToggleComplete = { /* No-op in search */ },
                                onTogglePin = { /* No-op in search */ },
                                onDelete = { /* No-op in search */ }
                            )
                        }
                    }
                    
                    if (notes.isNotEmpty()) {
                        item { SectionHeader("Notes", modifier = Modifier.padding(vertical = Spacing.small)) }
                        items(notes, key = { "note_${it.id}" }) { note ->
                            NoteCard(
                                note = note,
                                onClick = { navController.navigate("note_editor/${note.id}") },
                                onTogglePin = { /* No-op in search */ },
                                onDelete = { /* No-op in search */ }
                            )
                        }
                    }
                    
                    if (apps.isNotEmpty()) {
                        item { SectionHeader("Apps", modifier = Modifier.padding(vertical = Spacing.small)) }
                    }
                }

                items(apps, key = { it.packageName }) { app ->
                    AppTile(
                        app = app,
                        onClick = {
                            app.launchIntent?.let { intent ->
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
