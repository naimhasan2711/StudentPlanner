package com.binigrmay.studentplanner.ui.screens.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.binigrmay.studentplanner.ui.components.LectureCard
import com.binigrmay.studentplanner.ui.components.TaskCard
import com.binigrmay.studentplanner.viewmodel.LectureViewModel
import com.binigrmay.studentplanner.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Today screen showing today's tasks and lectures
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    taskViewModel: TaskViewModel,
    lectureViewModel: LectureViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToAddLecture: () -> Unit
) {
    val todaysTasks by taskViewModel.todaysTasks.collectAsState()
    val todaysLectures by lectureViewModel.todaysLectures.collectAsState()
    
    val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    val todayDate = dateFormat.format(Date())
    
    var showAddBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Today", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            todayDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddBottomSheet = true }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Lectures Section
            if (todaysLectures.isNotEmpty()) {
                item {
                    Text(
                        "Lectures",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                
                items(todaysLectures) { lecture ->
                    LectureCard(
                        lecture = lecture,
                        onClick = { /* Navigate to edit */ }
                    )
                }
            }
            
            // Tasks Section
            if (todaysTasks.isNotEmpty()) {
                item {
                    Text(
                        "Tasks & Deadlines",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = if (todaysLectures.isEmpty()) 16.dp else 8.dp)
                    )
                }
                
                items(todaysTasks) { task ->
                    TaskCard(
                        task = task,
                        onCheckedChange = { isChecked ->
                            taskViewModel.toggleTaskCompletion(task.id, isChecked)
                        },
                        onClick = { /* Navigate to edit */ }
                    )
                }
            }
            
            // Empty state
            if (todaysTasks.isEmpty() && todaysLectures.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "No events today!",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Add tasks or lectures to get started",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
        
        // Add Bottom Sheet
        if (showAddBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                ) {
                    // Title
                    Text(
                        text = "Create New",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Add Task Option
                    Surface(
                        onClick = {
                            showAddBottomSheet = false
                            onNavigateToAddTask()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Task",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                            Column {
                                Text(
                                    text = "Add Task",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Create a new task or deadline",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    // Add Lecture Option
                    Surface(
                        onClick = {
                            showAddBottomSheet = false
                            onNavigateToAddLecture()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Add Lecture",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(32.dp)
                            )
                            Column {
                                Text(
                                    text = "Add Lecture",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Schedule a new lecture or class",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
