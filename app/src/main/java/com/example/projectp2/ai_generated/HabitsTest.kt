package com.example.projectp2.ai_generated

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

/*
Generated almost exclusively with Claude 3.7 Sonnet
(I don't know why I even bother anymore)
 */

// Data class for Habit
data class Habit(
    val id: String,
    val title: String,
    val description: String,
    val color: Color,
    val frequency: String,
    val streak: Int,
    val completion: Float, // 0.0f to 1.0f
    val daysCompleted: List<LocalDate>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitTrackerScreen(habits: List<Habit> = sampleHabits()) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Daily", "Weekly", "Health", "Productivity", "Learning")

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Habits") },
                actions = {
                    IconButton(onClick = { /* TODO: Add habit */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Habit")
                    }
                    IconButton(onClick = { /* TODO: Open settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Filter Options Row
            FilterOptionsRow(
                filters = filters,
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            // 2. Calendar View
            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                habits = habits
            )

            // 3. Habit Cards List
            HabitsList(
                habits = habits.filter {
                    selectedFilter == "All" ||
                            it.frequency.contains(selectedFilter, ignoreCase = true) ||
                            it.title.contains(selectedFilter, ignoreCase = true)
                },
                selectedDate = selectedDate
            )
        }
    }
}

@Composable
fun FilterOptionsRow(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@Composable
fun CalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    habits: List<Habit>
) {
    val currentMonth = remember(selectedDate) {
        selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }
    val currentYear = remember(selectedDate) {
        selectedDate.year.toString()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Month and Year Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onDateSelected(selectedDate.minusMonths(1))
                }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
                }

                Text(
                    text = "$currentMonth $currentYear",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {
                    onDateSelected(selectedDate.plusMonths(1))
                }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
                }
            }

            // Days of Week Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (day in DayOfWeek.values()) {
                    Text(
                        text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            // Calendar Days
            val firstDayOfMonth = selectedDate.withDayOfMonth(1)
            val lastDayOfMonth = selectedDate.withDayOfMonth(selectedDate.month.length(selectedDate.isLeapYear))
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

            val totalDays = lastDayOfMonth.dayOfMonth
            val totalWeeks = (totalDays + firstDayOfWeek + 6) / 7

            for (week in 0 until totalWeeks) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (dayOfWeek in 0 until 7) {
                        val day = week * 7 + dayOfWeek - firstDayOfWeek + 1

                        if (day in 1..totalDays) {
                            val date = selectedDate.withDayOfMonth(day)
                            val isSelected = date.isEqual(selectedDate)
                            val hasHabits = habits.any { habit -> habit.daysCompleted.any { it.isEqual(date) } }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(percent = 50))
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            hasHabits -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isSelected || hasHabits) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        } else {
                            // Empty space for days outside current month
                            Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HabitsList(
    habits: List<Habit>,
    selectedDate: LocalDate
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Habits for ${selectedDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (habits.isEmpty()) {
            item {
                EmptyHabitsMessage()
            }
        } else {
            items(habits) { habit ->
                HabitCard1(
                    habit = habit,
                    isCompletedToday = habit.daysCompleted.any { it.isEqual(selectedDate) }
                )
            }
        }

        // Add space at the bottom
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HabitCard1(
    habit: Habit,
    isCompletedToday: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color indicator
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(habit.color)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Habit details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = habit.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = habit.frequency,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${habit.streak} days",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                LinearProgressIndicator(
                    progress = habit.completion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = habit.color
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Completion checkbox
            Checkbox(
                checked = isCompletedToday,
                onCheckedChange = { /* TODO: Toggle completion */ },
                colors = CheckboxDefaults.colors(
                    checkedColor = habit.color
                )
            )
        }
    }
}

@Composable
fun EmptyHabitsMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No habits for this day",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap the + button to create a new habit",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

// Sample data for preview
fun sampleHabits(): List<Habit> {
    return listOf(
        Habit(
            id = "1",
            title = "Morning Workout",
            description = "30 minutes of cardio exercise",
            color = Color(0xFF4CAF50),
            frequency = "Daily",
            streak = 12,
            completion = 0.85f,
            daysCompleted = listOf(
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(3),
                LocalDate.now()
            )
        ),
        Habit(
            id = "2",
            title = "Read a Book",
            description = "Read at least 20 pages",
            color = Color(0xFF2196F3),
            frequency = "Daily",
            streak = 7,
            completion = 0.6f,
            daysCompleted = listOf(
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(2)
            )
        ),
        Habit(
            id = "3",
            title = "Learn Kotlin",
            description = "Practice programming for 1 hour",
            color = Color(0xFFF44336),
            frequency = "Weekly",
            streak = 3,
            completion = 0.4f,
            daysCompleted = listOf(
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(14)
            )
        ),
        Habit(
            id = "4",
            title = "Meditate",
            description = "10 minutes of mindfulness",
            color = Color(0xFF9C27B0),
            frequency = "Daily",
            streak = 21,
            completion = 0.95f,
            daysCompleted = listOf(
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(2),
                LocalDate.now()
            )
        )
    )
}