package com.binigrmay.studentplanner.ui.screens.tasks

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.binigrmay.studentplanner.data.model.Priority
import com.binigrmay.studentplanner.data.model.Task
import com.binigrmay.studentplanner.ui.theme.PriorityHigh
import com.binigrmay.studentplanner.ui.theme.PriorityLow
import com.binigrmay.studentplanner.ui.theme.PriorityMedium
import com.binigrmay.studentplanner.ui.theme.PriorityUrgent
import com.binigrmay.studentplanner.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for adding a new task
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskViewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var category by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(System.currentTimeMillis() + 86400000) } // Tomorrow
    var showDatePicker by remember { mutableStateOf(false) }
    var expandedPriority by remember { mutableStateOf(false) }
    var reminderEnabled by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
            
            // Category Field
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                placeholder = { Text("e.g., Homework, Exam, Project") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Due Date Picker
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Due Date: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(dueDate))}")
            }
            
            // Priority Selector
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expandedPriority = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Priority: ${selectedPriority.name}")
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                }
                
                DropdownMenu(
                    expanded = expandedPriority,
                    onDismissRequest = { expandedPriority = false }
                ) {
                    Priority.values().forEach { priority ->
                        DropdownMenuItem(
                            text = { 
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(priority.name)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    // Priority indicator
                                    val color = when (priority) {
                                        Priority.LOW -> PriorityLow
                                        Priority.MEDIUM -> PriorityMedium
                                        Priority.HIGH -> PriorityHigh
                                        Priority.URGENT -> PriorityUrgent
                                    }
                                    Canvas(modifier = Modifier.size(12.dp)) {
                                        drawCircle(color = color)
                                    }
                                }
                            },
                            onClick = {
                                selectedPriority = priority
                                expandedPriority = false
                            }
                        )
                    }
                }
            }
            
            // Reminder Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Notifications, contentDescription = null)
                        Text("Enable Reminder")
                    }
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Save Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        taskViewModel.insertTask(
                            Task(
                                title = title,
                                description = description,
                                dueDate = dueDate,
                                priority = selectedPriority,
                                category = category.ifBlank { "General" },
                                reminderEnabled = reminderEnabled,
                                reminderTime = if (reminderEnabled) 60 else null // 1 hour before
                            )
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Task")
            }
        }
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dueDate = it }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
