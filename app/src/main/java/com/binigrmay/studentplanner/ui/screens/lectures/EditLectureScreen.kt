package com.binigrmay.studentplanner.ui.screens.lectures

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.binigrmay.studentplanner.R
import com.binigrmay.studentplanner.data.model.DayOfWeek
import com.binigrmay.studentplanner.data.model.Lecture
import com.binigrmay.studentplanner.viewmodel.LectureViewModel

/**
 * Helper function to format reminder time
 */
private fun formatReminderTime(minutes: Int): String {
    return when (minutes) {
        15 -> "15 minutes before"
        30 -> "30 minutes before"
        60 -> "1 hour before"
        120 -> "2 hours before"
        1440 -> "1 day before"
        2880 -> "2 days before"
        else -> "$minutes minutes before"
    }
}

/**
 * Screen for editing an existing lecture
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLectureScreen(
    lectureId: Int,
    lectureViewModel: LectureViewModel,
    onNavigateBack: () -> Unit
) {
    var lecture by remember { mutableStateOf<Lecture?>(null) }
    var title by remember { mutableStateOf("") }
    var instructor by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf(DayOfWeek.MONDAY) }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("10:30") }
    var expandedDay by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var isRecurring by remember { mutableStateOf(true) }
    var reminderEnabled by remember { mutableStateOf(false) }
    var reminderTime by remember { mutableStateOf(15) } // Default 15 minutes
    var expandedReminderTime by remember { mutableStateOf(false) }
    var color by remember { mutableStateOf("#6200EE") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    // Load the lecture
    LaunchedEffect(lectureId) {
        lectureViewModel.allLectures.collect { lectures ->
            lectures.find { it.id == lectureId }?.let {
                lecture = it
                title = it.title
                instructor = it.instructor
                room = it.room
                selectedDay = it.dayOfWeek
                startTime = it.startTime
                endTime = it.endTime
                isRecurring = it.isRecurring
                reminderEnabled = it.reminderEnabled
                reminderTime = it.reminderMinutesBefore
                color = it.color
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Lecture") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
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
                label = { Text("Lecture Title *") },
                placeholder = { Text("e.g., Mathematics 101") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Home, contentDescription = null) }
            )
            
            // Instructor Field
            OutlinedTextField(
                value = instructor,
                onValueChange = { instructor = it },
                label = { Text("Instructor") },
                placeholder = { Text("e.g., Dr. Smith") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
            )
            
            // Room Field
            OutlinedTextField(
                value = room,
                onValueChange = { room = it },
                label = { Text("Room No") },
                placeholder = { Text("e.g., Room 305") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_room), contentDescription = null) }
            )
            
            // Day of Week Selector
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expandedDay = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_clock), contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Day: ${selectedDay.name}")
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                }
                
                DropdownMenu(
                    expanded = expandedDay,
                    onDismissRequest = { expandedDay = false }
                ) {
                    DayOfWeek.values().forEach { day ->
                        DropdownMenuItem(
                            text = { Text(day.name) },
                            onClick = {
                                selectedDay = day
                                expandedDay = false
                            }
                        )
                    }
                }
            }
            
            // Time Pickers Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Start Time
                OutlinedButton(
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Start Time", style = MaterialTheme.typography.bodySmall)
                        Text(startTime, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                
                // End Time
                OutlinedButton(
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("End Time", style = MaterialTheme.typography.bodySmall)
                        Text(endTime, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            
            // Recurring Toggle
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
                        Icon(Icons.Filled.Refresh, contentDescription = null)
                        Text("Recurring Weekly")
                    }
                    Switch(
                        checked = isRecurring,
                        onCheckedChange = { isRecurring = it }
                    )
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
                                    15 to "15 minutes before",
                                    30 to "30 minutes before",
                                    60 to "1 hour before",
                                    120 to "2 hours before",
                                    1440 to "1 day before",
                                    2880 to "2 days before"
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
            
            // Update Button
            Button(
                onClick = {
                    lecture?.let {
                        lectureViewModel.updateLecture(
                            it.copy(
                                title = title,
                                instructor = instructor,
                                room = room,
                                dayOfWeek = selectedDay,
                                startTime = startTime,
                                endTime = endTime,
                                isRecurring = isRecurring,
                                reminderEnabled = reminderEnabled,
                                reminderMinutesBefore = if (reminderEnabled) reminderTime else 15,
                                color = color
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
                Text("Update Lecture")
            }
        }
    }
    
    // Start Time Picker Dialog
    if (showStartTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = startTime.split(":")[0].toIntOrNull() ?: 9,
            initialMinute = startTime.split(":")[1].toIntOrNull() ?: 0,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showStartTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    showStartTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartTimePicker = false }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
    
    // End Time Picker Dialog
    if (showEndTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = endTime.split(":")[0].toIntOrNull() ?: 10,
            initialMinute = endTime.split(":")[1].toIntOrNull() ?: 30,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showEndTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    showEndTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndTimePicker = false }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
    
    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
            title = { Text("Delete Lecture") },
            text = { Text("Are you sure you want to delete this lecture? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        lecture?.let { lectureViewModel.deleteLecture(it) }
                        showDeleteDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
