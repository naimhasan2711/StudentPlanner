package com.binigrmay.studentplanner.ui.screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.binigrmay.studentplanner.data.model.DayOfWeek
import com.binigrmay.studentplanner.ui.components.LectureCard
import com.binigrmay.studentplanner.viewmodel.LectureViewModel
import com.binigrmay.studentplanner.viewmodel.TaskViewModel

/**
 * Calendar screen showing weekly schedule
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    taskViewModel: TaskViewModel,
    lectureViewModel: LectureViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToAddLecture: () -> Unit
) {
    val recurringLectures by lectureViewModel.recurringLectures.collectAsState()
    val selectedDayLectures by lectureViewModel.selectedDayLectures.collectAsState()
    
    var selectedDay by remember { mutableStateOf(DayOfWeek.MONDAY) }
    var expandedDayDropdown by remember { mutableStateOf(false) }
    var showAddBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    
    LaunchedEffect(selectedDay) {
        lectureViewModel.loadLecturesForDay(selectedDay)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weekly Schedule") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddBottomSheet = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Day Selector Dropdown
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        onClick = { expandedDayDropdown = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 0.dp,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = "Select Day",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedDay.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Expand",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    
                    DropdownMenu(
                        expanded = expandedDayDropdown,
                        onDismissRequest = { expandedDayDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        DayOfWeek.values().forEach { day ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = day.name.lowercase().replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (day == selectedDay) FontWeight.Bold else FontWeight.Normal,
                                            color = if (day == selectedDay) 
                                                MaterialTheme.colorScheme.onSurface
                                            else 
                                                MaterialTheme.colorScheme.onSurface
                                        )
                                        if (day == selectedDay) {
                                            Icon(
                                                Icons.Filled.Build,
                                                contentDescription = "Selected",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    selectedDay = day
                                    expandedDayDropdown = false
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }
            
            // Lectures for selected day
            if (selectedDayLectures.isEmpty()) {
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
                            "No lectures on ${selectedDay.name.lowercase().replaceFirstChar { it.uppercase() }}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Add a lecture to this day",
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
                    items(selectedDayLectures) { lecture ->
                        LectureCard(
                            lecture = lecture,
                            onClick = { /* Navigate to edit */ }
                        )
                    }
                    
                    // Bottom spacing for FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
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
                                imageVector = Icons.Filled.Build,
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
