package com.binigrmay.studentplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.binigrmay.studentplanner.R
import com.binigrmay.studentplanner.data.model.Lecture

/**
 * Reusable card component for displaying a lecture with modern design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureCard(
    lecture: Lecture,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lectureColor = Color(android.graphics.Color.parseColor(lecture.color))
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Colored left accent bar
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(lectureColor)
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title with colored background chip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = lecture.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (lecture.isRecurring) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = lectureColor.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_refresh),
                                    contentDescription = "Recurring",
                                    modifier = Modifier.size(14.dp),
                                    tint = lectureColor
                                )
                                Text(
                                    text = "Weekly",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = lectureColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                
                // Time with elegant styling
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = lectureColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = lectureColor.copy(alpha = 0.2f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock),
                                contentDescription = "Time",
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(8.dp),
                                tint = lectureColor
                            )
                        }
                        Text(
                            text = "${lecture.startTime} - ${lecture.endTime}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = lectureColor
                        )
                    }
                }
                
                // Instructor and Room in a row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Instructor
                    if (lecture.instructor.isNotEmpty()) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Instructor",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = lecture.instructor,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    
                    // Room
                    if (lecture.room.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_room),
                                contentDescription = "Room",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = lecture.room,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                
                // Notes (if available)
                if (lecture.notes.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = lecture.notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(8.dp),
                            maxLines = 2
                        )
                    }
                }
                
                // Footer with day and reminder
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = lectureColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = lecture.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = lectureColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                    
                    if (lecture.reminderEnabled) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_remainder),
                                contentDescription = "Reminder",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = "${lecture.reminderMinutesBefore} min before",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
