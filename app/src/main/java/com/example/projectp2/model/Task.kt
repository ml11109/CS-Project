package com.example.projectp2.model

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Task(
    val habit: Habit,
    val index: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val date: LocalDate,
    var status: String = CompletionStatus.PENDING,
    var notes: String = ""
) {
    fun isOngoing(): Boolean {
        return this.status == CompletionStatus.PENDING
                && (this.date == LocalDate.now()
                    && this.startTime.isBefore(LocalTime.now())
                    && this.endTime.isAfter(LocalTime.now()))
    }

    fun isUpcoming(): Boolean {
        return this.status == CompletionStatus.PENDING
                && (this.date.isAfter(LocalDate.now())
                    || (this.date == LocalDate.now() && this.startTime.isAfter(LocalTime.now())))
    }

    fun isCompleted(): Boolean {
        return this.status in listOf(CompletionStatus.COMPLETED, CompletionStatus.SKIPPED)
    }

    fun isFailed(): Boolean {
        return this.status == CompletionStatus.PENDING
                && (this.date.isBefore(LocalDate.now())
                    || (this.date == LocalDate.now() && this.endTime.isBefore(LocalTime.now())))
    }

    fun isCompletable(): Boolean {
        return this.isOngoing() || (this.isUpcoming() && this.habit.advancedSettings.allowAdvanceCompletion)
    }
}

@Composable
fun TaskCard(userDataViewModel: UserDataViewModel, task: Task, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Task details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(percent = 50))
                            .background(userDataViewModel.getCategoryColor(task.habit.category))
                    )
                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "${task.habit.title} (${task.index}/${task.habit.taskList.tasks.size})",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))

                    Checkbox(
                        modifier = Modifier.size(20.dp),
                        checked = task.status == CompletionStatus.COMPLETED,
                        onCheckedChange = null
                    )
                }

                if (task.habit.description.isNotBlank()) {
                    Text(
                        text = task.habit.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.startTime.format(DateTimeFormatter.ofPattern("hh:mm a")) + " - "
                                + task.endTime.format(DateTimeFormatter.ofPattern("hh:mm a")) + ", "
                                + task.date.format(DateTimeFormatter.ofPattern("dd/MM")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleTaskCard(userDataViewModel: UserDataViewModel, task: Task, modifier: Modifier = Modifier, cornerSize: Dp = 12.dp) {
    Box(
        modifier = modifier.shadow(2.dp).background(MaterialTheme.colorScheme.surfaceTint, shape = RoundedCornerShape(4.dp)),
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(cornerSize.toPx(), 0f)
                    lineTo(0f, cornerSize.toPx())
                    close()
                },
                color = userDataViewModel.getCategoryColor(task.habit.category)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = " ${task.habit.title} (${task.index}/${task.habit.taskList.tasks.size})",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun TaskCompletionDialog(task: Task, onDismiss: () -> Unit) {
    var status by remember { mutableStateOf(task.status) }
    var notes by remember { mutableStateOf(task.notes) }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = { Text("Task Completion") },

        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Completed", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.weight(1f))
                    Checkbox(
                        checked = status == CompletionStatus.COMPLETED,
                        onCheckedChange = {
                            status = if (it) CompletionStatus.COMPLETED else CompletionStatus.PENDING
                            task.status = status
                        }
                    )
                }

                val numExceptionsUsed = task.habit.taskList.getNumExceptionsUsed(task.date)
                val numExceptions = task.habit.advancedSettings.numExceptionsPerMonth

                if (task.habit.advancedSettings.allowExceptions && numExceptionsUsed < numExceptions
                    && task.status != CompletionStatus.SKIPPED) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Skipped ($numExceptionsUsed/$numExceptions)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.weight(1f))
                        Checkbox(
                            checked = status == CompletionStatus.SKIPPED,
                            onCheckedChange = {
                                status = if (it) CompletionStatus.SKIPPED else CompletionStatus.PENDING
                                task.status = CompletionStatus.SKIPPED
                            }
                        )
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") }
                )
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    task.status = status
                    task.notes = notes
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
