package com.binigrmay.studentplanner.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.binigrmay.studentplanner.data.model.Priority
import com.binigrmay.studentplanner.data.model.Task
import com.binigrmay.studentplanner.ui.theme.PriorityHigh
import com.binigrmay.studentplanner.ui.theme.PriorityLow
import com.binigrmay.studentplanner.ui.theme.PriorityMedium
import com.binigrmay.studentplanner.ui.theme.PriorityUrgent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Reusable card component for displaying a task
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = onCheckedChange
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (task.isCompleted) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                    
                    if (task.description.isNotEmpty()) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        // Priority Badge
                        PriorityBadge(priority = task.priority)
                        
                        // Due Date
                        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        Text(
                            text = dateFormat.format(Date(task.dueDate)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        // Category
                        if (task.category.isNotEmpty()) {
                            Text(
                                text = "• ${task.category}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            if (onDelete != null) {
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete task",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteDialog && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
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

/**
 * Priority badge component
 */
@Composable
fun PriorityBadge(priority: Priority) {
    val color = when (priority) {
        Priority.LOW -> PriorityLow
        Priority.MEDIUM -> PriorityMedium
        Priority.HIGH -> PriorityHigh
        Priority.URGENT -> PriorityUrgent
    }
    
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = priority.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
