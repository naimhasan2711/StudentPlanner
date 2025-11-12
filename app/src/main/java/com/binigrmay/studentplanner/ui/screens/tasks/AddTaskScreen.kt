package com.binigrmay.studentplanner.ui.screens.tasks

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.binigrmay.studentplanner.R
import com.binigrmay.studentplanner.data.model.Priority
import com.binigrmay.studentplanner.data.model.Task
import com.binigrmay.studentplanner.ui.theme.PriorityHigh
import com.binigrmay.studentplanner.ui.theme.PriorityLow
import com.binigrmay.studentplanner.ui.theme.PriorityMedium
import com.binigrmay.studentplanner.ui.theme.PriorityUrgent
import com.binigrmay.studentplanner.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Helper function to format reminder time
 */
private fun formatReminderTime(minutes: Long): String {
    return when (minutes) {
        15L -> "15 minutes before"
        30L -> "30 minutes before"
        60L -> "1 hour before"
        120L -> "2 hours before"
        1440L -> "1 day before"
        2880L -> "2 days before"
        else -> "$minutes minutes before"
    }
}

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
    var reminderTime by remember { mutableStateOf(60L) } // Default 1 hour
    var expandedReminderTime by remember { mutableStateOf(false) }
    
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
                Icon(painter = painterResource(id = R.drawable.ic_clock), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Due Date: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(dueDate))}")
            }
            
            // Priority Selector
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expandedPriority = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_priority), contentDescription = null)
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_remainder), contentDescription = null)
                            Text("Enable Reminder")
                        }
                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = { reminderEnabled = it }
                        )
                    }
                    
                    // Reminder Time Selector (only shown when reminder is enabled)
                    if (reminderEnabled) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { expandedReminderTime = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Filled.AccountBox, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Remind me: ${formatReminderTime(reminderTime)}")
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                            }
                            
                            DropdownMenu(
                                expanded = expandedReminderTime,
                                onDismissRequest = { expandedReminderTime = false }
                            ) {
                                val reminderOptions = listOf(
                                    15L to "15 minutes before",
                                    30L to "30 minutes before",
                                    60L to "1 hour before",
                                    120L to "2 hours before",
                                    1440L to "1 day before",
                                    2880L to "2 days before"
                                )
                                
                                reminderOptions.forEach { (minutes, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = {
                                            reminderTime = minutes
                                            expandedReminderTime = false
                                        },
                                        trailingIcon = {
                                            if (reminderTime == minutes) {
                                                Icon(
                                                    Icons.Filled.Build,
                                                    contentDescription = "Selected",
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
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
                                reminderTime = if (reminderEnabled) reminderTime else null
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
