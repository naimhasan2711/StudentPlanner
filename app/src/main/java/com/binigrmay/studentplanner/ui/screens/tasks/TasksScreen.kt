package com.binigrmay.studentplanner.ui.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.binigrmay.studentplanner.ui.components.TaskCard
import com.binigrmay.studentplanner.viewmodel.TaskViewModel

/**
 * Tasks screen showing all tasks with filter options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TaskViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToEditTask: (Int) -> Unit
) {
    val allTasks by viewModel.allTasks.collectAsState()
    val incompleteTasks by viewModel.incompleteTasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Incomplete", "Completed")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                actions = {
                    if (completedTasks.isNotEmpty()) {
                        IconButton(onClick = { viewModel.deleteCompletedTasks() }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete completed tasks"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddTask) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            // Task List
            val tasksToShow = when (selectedTab) {
                0 -> allTasks
                1 -> incompleteTasks
                2 -> completedTasks
                else -> allTasks
            }
            
            if (tasksToShow.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = when (selectedTab) {
                                0 -> "No tasks yet"
                                1 -> "No incomplete tasks"
                                2 -> "No completed tasks"
                                else -> "No tasks"
                            },
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Tap + to add a new task",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tasksToShow, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = { isChecked ->
                                viewModel.toggleTaskCompletion(task.id, isChecked)
                            },
                            onClick = { onNavigateToEditTask(task.id) },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                    
                    // Bottom spacing for FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
