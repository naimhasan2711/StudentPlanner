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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import java.util.Calendar

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
 * Screen for adding a new lecture
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLectureScreen(
    lectureViewModel: LectureViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var instructor by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    // Get current day of week
    val currentDayOfWeek = remember {
        DayOfWeek.fromCalendar(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
    }
    var selectedDay by remember { mutableStateOf(currentDayOfWeek) }
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
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Lecture") },
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
                label = { Text("Lecture Title *") },
                placeholder = { Text("e.g., Mathematics 101") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_title), contentDescription = null) }
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
                            },
                            trailingIcon = {
                                if (selectedDay == day) {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = "Selected",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
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
                        Icon(painter = painterResource(id = R.drawable.ic_refresh), contentDescription = null)
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
                                Icon(painter = painterResource(id = R.drawable.ic_remainder), contentDescription = null)
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
                                                    Icons.Filled.Check,
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
                    lectureViewModel.insertLecture(
                        Lecture(
                            title = title,
                            instructor = instructor.ifBlank { "TBA" },
                            room = room,
                            dayOfWeek = selectedDay,
                            startTime = startTime,
                            endTime = endTime,
                            color = color,
                            isRecurring = isRecurring,
                            reminderEnabled = reminderEnabled,
                            reminderMinutesBefore = if (reminderEnabled) reminderTime else 15
                        )
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Lecture")
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
}
