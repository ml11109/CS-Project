package com.example.projectp2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.CustomDropdownTextField
import com.example.projectp2.composables.ExpandingTextField
import com.example.projectp2.composables.OptionsRow
import com.example.projectp2.composables.TimePickerButton
import com.example.projectp2.model.Category
import com.example.projectp2.model.Frequency
import com.example.projectp2.model.Habit
import com.example.projectp2.model.TaskList
import com.example.projectp2.model.UserDataViewModel
import kotlinx.coroutines.CoroutineScope
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DetailsScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope, habitId: Int) {
    val focusManager = LocalFocusManager.current
    val habit = userDataViewModel.getHabitFromId(habitId)
    var frequency by remember { mutableStateOf(habit.frequency) }

    AppScaffold(
        title = if (habitId == -1) "New Habit" else "Edit Habit",
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) { nestedScrollConnection ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleTextField(habit, Modifier.fillMaxWidth())
            DescriptionTextField(habit, Modifier.fillMaxWidth())

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Category", Modifier.width(80.dp))
                CategoryTextField(userDataViewModel, navController, habit, Modifier.fillMaxWidth())
            }

            HorizontalDivider(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Frequency", Modifier.width(80.dp))
                Spacer(Modifier.weight(1f))
                FrequencySelector(userDataViewModel, habit, Modifier.fillMaxWidth()) { frequency = it }
            }

            TimeSelector(habit, Modifier.fillMaxWidth().heightIn(0.dp, 400.dp))

            if (frequency == Frequency.WEEKLY) {
                Text("Days")
                DayOfWeekSelector(habit, Modifier.fillMaxWidth())
            } else if (frequency == Frequency.MONTHLY) {
                Text("Days")
                DayOfMonthSelector(habit, Modifier.fillMaxWidth())
            }

            HorizontalDivider(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.width(100.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceBright,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Cancel", style = MaterialTheme.typography.titleMedium)
                }

                Button(
                    onClick = {
                        userDataViewModel.habits[habit.id] = habit
                        navController.popBackStack()
                    },
                    modifier = Modifier.width(100.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Save", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun TitleTextField(habit: Habit, modifier: Modifier = Modifier) {
    BoxWithConstraints {
        val boxWithConstraintsScope = this
        ExpandingTextField(
            modifier = modifier,
            hint = "Enter title...",
            width = boxWithConstraintsScope.maxWidth - 44.dp,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            showHintIfEmpty = true
        ) {
            habit.title = it
        }
    }
}

@Composable
fun DescriptionTextField(habit: Habit, modifier: Modifier = Modifier) {
    var description by remember { mutableStateOf(habit.description) }

    OutlinedTextField(
        value = description,
        onValueChange = { description = it; habit.description = it },
        label = { Text("Description") },
        minLines = 2,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}

@Composable
fun CategoryTextField(userDataViewModel: UserDataViewModel, navController: NavController, habit: Habit, modifier: Modifier = Modifier) {
    CustomDropdownTextField(
        options = userDataViewModel.categories,
        textStyle = MaterialTheme.typography.bodyLarge,
        initialOption = if (habit.category == Category.NONE) "Select a category" else habit.category,
        navController = navController
    ) {
        habit.category = it
    }
}

@Composable
fun FrequencySelector(userDataViewModel: UserDataViewModel, habit: Habit, modifier: Modifier = Modifier, onFrequencyChange: (String) -> Unit = {}) {
    OptionsRow(userDataViewModel.frequencyTypes, modifier, habit.frequency) { onFrequencyChange(it); habit.frequency = it }
}

@Composable
fun TimeSelector(habit: Habit, modifier: Modifier = Modifier) {
    val startTimes = remember { mutableStateListOf<LocalTime>().apply { addAll(habit.taskList.startTimes) } }
    val endTimes = remember { mutableStateListOf<LocalTime>().apply { addAll(habit.taskList.endTimes) } }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Times")
            IconButton(
                onClick = {
                    startTimes.add(LocalTime.of(0, 0))
                    endTimes.add(LocalTime.of(0, 0))
                    habit.taskList.startTimes.add(LocalTime.of(0, 0))
                    habit.taskList.endTimes.add(LocalTime.of(0, 0))
                }
            ) {
                Icon(Icons.Default.Add, "Add")
            }
        }

        LazyColumn {
            items(startTimes.size) { index ->
                Row(
                    modifier.fillMaxWidth().padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(startTimes[index].format(DateTimeFormatter.ofPattern("hh:mm a")))
                    TimePickerButton(
                        time = startTimes[index],
                        modifier = Modifier.size(30.dp)
                    ) { _, hour, minute ->
                        startTimes[index] = LocalTime.of(hour, minute)
                        habit.taskList.startTimes[index] = startTimes[index]
                    }

                    Text("to")
                    Text(endTimes[index].format(DateTimeFormatter.ofPattern("hh:mm a")))
                    TimePickerButton(
                        time = endTimes[index],
                        modifier = Modifier.size(30.dp)
                    ) { _, hour, minute ->
                        endTimes[index] = LocalTime.of(hour, minute)
                        habit.taskList.endTimes[index] = endTimes[index]
                    }

                    IconButton(
                        onClick = {
                            startTimes.removeAt(index)
                            endTimes.removeAt(index)
                            habit.taskList.startTimes.removeAt(index)
                            habit.taskList.endTimes.removeAt(index)
                        }
                    ) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun DayOfWeekSelector(habit: Habit, modifier: Modifier = Modifier) {
    val selectedDays = remember { mutableStateListOf<Int>().apply { addAll(habit.taskList.daysOfWeek) } }

    Row(
        modifier = modifier
    ) {
        for (day in 1..7) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(
                        if (day in selectedDays) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable {
                        if (day in selectedDays) {
                            selectedDays.remove(day)
                            habit.taskList.daysOfMonth.remove(day)
                        } else {
                            selectedDays.add(day)
                            habit.taskList.daysOfMonth.add(day)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = TaskList.daysOfWeek[day]!![0].toString(),
                    color = if (day in selectedDays) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (day in selectedDays) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun DayOfMonthSelector(habit: Habit, modifier: Modifier = Modifier) {
    val selectedDays = remember { mutableStateListOf<Int>().apply { addAll(habit.taskList.daysOfMonth) } }

    for (week in 0..4) {
        Row(
            modifier = modifier
        ) {
            for (dayOfWeek in 1..7) {
                val day = week * 7 + dayOfWeek

                if (day in 1..31) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(percent = 50))
                            .background(
                                if (day in selectedDays) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable {
                                if (day in selectedDays) {
                                    selectedDays.remove(day)
                                    habit.taskList.daysOfMonth.remove(day)
                                } else {
                                    selectedDays.add(day)
                                    habit.taskList.daysOfMonth.add(day)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            color = if (day in selectedDays) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                }
            }
        }
    }
}
