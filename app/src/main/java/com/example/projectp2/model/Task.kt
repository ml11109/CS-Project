package com.example.projectp2.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Task(
    val habit: Habit,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val date: LocalDate,
    val completed: Boolean = false,
    val exempted: Boolean = false,
    val notes: String = ""
) {
    fun isOngoing(): Boolean {
        return (
            this.date == LocalDate.now()
            && this.startTime.isBefore(LocalTime.now())
            && this.endTime.isAfter(LocalTime.now())
        )
    }

    fun isCompleted(): Boolean {
        return (
            this.date.isBefore(LocalDate.now())
            || (
                this.date == LocalDate.now()
                && this.endTime.isBefore(LocalTime.now())
            )
        )
    }

    fun isUpcoming(): Boolean {
        return (
            this.date.isAfter(LocalDate.now())
            || (
                this.date == LocalDate.now()
                && this.startTime.isAfter(LocalTime.now())
            )
        )
    }
}

@Composable
fun TaskCard(userDataViewModel: UserDataViewModel, task: Task, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            .size(20.dp)
                            .clip(RoundedCornerShape(percent = 50))
                            .background(userDataViewModel.getCategoryColor(task.habit.category))
                    )
                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = task.habit.title,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))

                    Checkbox(
                        modifier = Modifier.size(20.dp),
                        checked = task.completed,
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
