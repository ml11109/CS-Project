package com.example.projectp2.model

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Task(
    val habitId: Int,
    val index: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val date: LocalDate,
    var status: String = CompletionStatus.PENDING,
    var notes: String = ""
) : Serializable {
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

    fun isCompletable(userDataViewModel: UserDataViewModel): Boolean {
        val habit = userDataViewModel.getHabitFromId(this.habitId)
        return this.isOngoing() || (this.isUpcoming() && habit.advancedSettings.allowAdvanceCompletion)
    }
}

@Composable
fun TaskCard(userDataViewModel: UserDataViewModel, task: Task, modifier: Modifier = Modifier) {
    val habit = userDataViewModel.getHabitFromId(task.habitId)

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright)
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(percent = 50))
                            .background(userDataViewModel.getCategoryColor(habit.category))
                    )
                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "${habit.title} (${task.index}/${habit.taskList.tasks.size})",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(8.dp))

                    Checkbox(
                        modifier = Modifier.size(20.dp),
                        checked = task.status == CompletionStatus.COMPLETED,
                        onCheckedChange = null
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
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleTaskCard(userDataViewModel: UserDataViewModel, task: Task, modifier: Modifier = Modifier, cornerSize: Dp = 12.dp) {
    val habit = userDataViewModel.getHabitFromId(task.habitId)

    Box(
        modifier = modifier.shadow(2.dp).background(MaterialTheme.colorScheme.surfaceBright, shape = RoundedCornerShape(4.dp)),
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(cornerSize.toPx(), 0f)
                    lineTo(0f, cornerSize.toPx())
                    close()
                },
                color = userDataViewModel.getCategoryColor(habit.category)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = " ${habit.title}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )

            if (task.isCompleted()) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp)
                )
            } else if (task.isFailed()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Failed",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun TaskCompletionDialog(userDataViewModel: UserDataViewModel, task: Task, onDismiss: () -> Unit) {
    val habit = userDataViewModel.getHabitFromId(task.habitId)
    var status by remember { mutableStateOf(task.status) }
    var notes by remember { mutableStateOf(task.notes) }
    val context = LocalContext.current

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
                            if (!task.isCompletable(userDataViewModel)) return@Checkbox
                            status = if (it) CompletionStatus.COMPLETED else CompletionStatus.PENDING
                        }
                    )
                }

                val numExceptionsUsed = habit.taskList.getNumExceptionsUsed(task.date)
                val numExceptions = habit.advancedSettings.numExceptionsPerMonth

                if (habit.advancedSettings.allowExceptions && numExceptionsUsed < numExceptions
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
                                if (!task.isCompletable(userDataViewModel)) return@Checkbox
                                status = if (it) CompletionStatus.SKIPPED else CompletionStatus.PENDING
                            }
                        )
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = notes,
                    enabled = task.isCompletable(userDataViewModel),
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = TextFieldDefaults.colors().unfocusedTextColor,
                        disabledContainerColor = TextFieldDefaults.colors().unfocusedContainerColor,
                        disabledLabelColor = TextFieldDefaults.colors().unfocusedLabelColor,
                        disabledIndicatorColor = TextFieldDefaults.colors().unfocusedIndicatorColor
                    )
                )
            }
        },

        confirmButton = {
            Button(
                enabled = task.isCompletable(userDataViewModel),
                onClick = {
                    task.status = status
                    task.notes = notes
                    userDataViewModel.updateHabitCompletion(context)
                    userDataViewModel.saveHabits(context)
                    Toast.makeText(context, "Task details saved", Toast.LENGTH_SHORT).show()
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
