package com.example.projectp2.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.BasicAlertDialog
import com.example.projectp2.composables.CustomDropdownSelector
import com.example.projectp2.composables.ExpandingTextField
import com.example.projectp2.composables.OptionsRow
import com.example.projectp2.composables.TimePickerButton
import com.example.projectp2.model.AdvancedSettings
import com.example.projectp2.model.Category
import com.example.projectp2.model.Frequency
import com.example.projectp2.model.Habit
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Week
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DetailsScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope, habitType: Int, oldHabit: Habit) {
    val habit = oldHabit.copy()
    val isNewHabit = habitType != 1
    var valid by remember { mutableStateOf(false) }
    var advancedSettings by remember { mutableStateOf(habit.advancedSettings.copy()) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    fun checkValidity() {
        valid = true

        if (habit.title.isBlank() || habit.category == Category.NONE || habit.frequency == Frequency.NONE) valid = false

        if (habit.taskList.startTimes.size == 0) valid = false
        if (habit.frequency == Frequency.WEEKLY && !habit.taskList.daysOfWeek.values.contains(true)) valid = false
        if (habit.frequency == Frequency.MONTHLY && !habit.taskList.daysOfMonth.values.contains(true)) valid = false

        if (habit.taskList.startDate.isAfter(habit.taskList.endDate)) valid = false
        if (habit.taskList.startDate.isBefore(LocalDate.now())) valid = false
        if (!isNewHabit && habit.taskList.startDate != oldHabit.taskList.startDate) valid = false

        for (index in habit.taskList.startTimes.indices) {
            if (habit.taskList.startTimes[index].isAfter(habit.taskList.endTimes[index])) {
                valid = false; break
            }
        }
    }

    fun saveHabit(context: Context) {
        habit.id = userDataViewModel.getHabitId()
        habit.advancedSettings = advancedSettings

        if (isNewHabit) {
            userDataViewModel.habits.add(habit)
            habit.taskList.createTasks(context, userDataViewModel, habit)
        } else {
            userDataViewModel.habits[userDataViewModel.habits.indexOf(oldHabit)] = habit
            habit.taskList.updateTasks(context, userDataViewModel, oldHabit, habit)
        }

        navController.popBackStack()
        userDataViewModel.saveHabits(context)
        Toast.makeText(context, "Habit saved", Toast.LENGTH_SHORT).show()
    }

    AppScaffold(
        title = if (isNewHabit) "New Habit" else "Edit Habit",
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) { nestedScrollConnection ->
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .nestedScroll(nestedScrollConnection)
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectTapGestures { focusManager.clearFocus() }
                    },
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BasicDetails(userDataViewModel, navController, habit, Modifier.fillMaxWidth()) { checkValidity() }
                HorizontalDivider(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 12.dp))

                TimingDetails(userDataViewModel, navController, habit, oldHabit, isNewHabit, Modifier.fillMaxWidth()) { checkValidity() }
                HorizontalDivider(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 12.dp))

                AdvancedSettingsBox(advancedSettings, Modifier.fillMaxWidth()) { advancedSettings = it; checkValidity() }
                HorizontalDivider(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 12.dp))

                DetailsButtons(userDataViewModel, navController, habit, oldHabit, isNewHabit, valid,
                    Modifier.fillMaxWidth().padding(end = 16.dp), { checkValidity() }, { saveHabit(context) })
            }
        }

        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .nestedScroll(nestedScrollConnection)
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectTapGestures { focusManager.clearFocus() }
                    },
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicDetails(userDataViewModel, navController, habit, Modifier.weight(1f)) { checkValidity() }
                    Spacer(Modifier.width(24.dp))
                    TimingDetails(userDataViewModel, navController, habit, oldHabit, isNewHabit, Modifier.weight(1f)) { checkValidity() }
                }
                HorizontalDivider(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 12.dp))

                AdvancedSettingsBox(advancedSettings, Modifier.fillMaxWidth()) { advancedSettings = it; checkValidity() }
                HorizontalDivider(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 12.dp))

                DetailsButtons(userDataViewModel, navController, habit, oldHabit, isNewHabit, valid,
                    Modifier.fillMaxWidth().padding(end = 16.dp), { checkValidity() }, { saveHabit(context) })
            }
        }
    }
}

@Composable
fun BasicDetails(userDataViewModel: UserDataViewModel, navController: NavController, habit: Habit, modifier: Modifier = Modifier, checkValidity: () -> Unit) {
    // Title, description and category

    Column(
        modifier = modifier
    ) {
        TitleTextField(habit, Modifier.fillMaxWidth()) { checkValidity() }
        DescriptionTextField(habit, Modifier.fillMaxWidth()) { checkValidity() }
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Category", Modifier.width(90.dp))
            Spacer(Modifier.weight(1f))
            CategoryTextField(userDataViewModel, navController, habit, Modifier.fillMaxWidth()) { checkValidity() }
        }
    }
}

@Composable
fun TimingDetails(
    userDataViewModel: UserDataViewModel, navController: NavController,
    habit: Habit, oldHabit: Habit, isNewHabit: Boolean,
    modifier: Modifier = Modifier, checkValidity: () -> Unit
) {
    var frequency by remember { mutableStateOf(habit.frequency) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Frequency
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Frequency", Modifier.width(90.dp))
            Spacer(Modifier.weight(1f))
            FrequencySelector(userDataViewModel, habit) {
                frequency = it; habit.frequency = it; checkValidity()
            }
            Spacer(Modifier.weight(1f))
        }

        // Time and date
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Time period", Modifier.width(90.dp))
            Spacer(Modifier.weight(1f))
            DateSelector(habit, oldHabit, isNewHabit) { checkValidity() }
            Spacer(Modifier.weight(1f))
        }

        TimeSelector(habit, Modifier.fillMaxWidth().heightIn(0.dp, 400.dp)) { checkValidity() }

        if (frequency == Frequency.WEEKLY) {
            Text("Days")
            DayOfWeekSelector(habit, Modifier.fillMaxWidth()) { checkValidity() }
        } else if (frequency == Frequency.MONTHLY) {
            Text("Days")
            DayOfMonthSelector(habit, Modifier.fillMaxWidth()) { checkValidity() }
        }
    }
}

@Composable
fun AdvancedSettingsBox(
    advancedSettings: AdvancedSettings, modifier: Modifier = Modifier,
    onSettingsChange: (advancedSettings: AdvancedSettings) -> Unit
) {
    var showAdvancedSettings by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clickable { showAdvancedSettings = !showAdvancedSettings },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Advanced Settings",
                style = MaterialTheme.typography.titleMedium
            )

            Icon(
                if (showAdvancedSettings) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (showAdvancedSettings) "Hide advanced settings" else "Show advanced settings"
            )
        }

        AnimatedVisibility(visible = showAdvancedSettings) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    // Send notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Notifications")
                        Spacer(Modifier.weight(1f))
                        Checkbox(
                            checked = advancedSettings.sendNotifications,
                            onCheckedChange = {
                                onSettingsChange(advancedSettings.copy(sendNotifications = it))
                            }
                        )
                    }
                    Spacer(Modifier.height(12.dp))

                    // Allow exceptions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Exceptions")
                        Spacer(Modifier.weight(1f))

                        OutlinedTextField(
                            value = if (advancedSettings.numExceptionsPerMonth == 0) "" else advancedSettings.numExceptionsPerMonth.toString(),
                            onValueChange = { newValue ->
                                if (newValue.isEmpty() || newValue.isDigitsOnly() && newValue.length <= 2) {
                                    onSettingsChange(advancedSettings.copy(numExceptionsPerMonth = if (newValue.isEmpty()) 0 else newValue.toInt()))
                                }
                            },
                            modifier = Modifier.width(54.dp).height(54.dp),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        Text("per month", Modifier.padding(start = 12.dp, end = 16.dp))

                        Checkbox(
                            checked = advancedSettings.allowExceptions,
                            onCheckedChange = {
                                onSettingsChange(advancedSettings.copy(allowExceptions = it))
                            }
                        )
                    }
                    Spacer(Modifier.height(12.dp))

                    // Allow advance completion
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Allow advance completion")
                        Spacer(Modifier.weight(1f))
                        Checkbox(
                            checked = advancedSettings.allowAdvanceCompletion,
                            onCheckedChange = {
                                onSettingsChange(advancedSettings.copy(allowAdvanceCompletion = it))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsButtons(
    userDataViewModel: UserDataViewModel, navController: NavController,
    habit: Habit, oldHabit: Habit, isNewHabit: Boolean, valid: Boolean,
    modifier: Modifier = Modifier,
    checkValidity: () -> Unit, saveHabit: () -> Unit
) {
    // Cancel and save buttons
    Row(
        modifier = modifier,
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
        Spacer(Modifier.width(12.dp))

        var showAlertDialog by remember { mutableStateOf(false) }
        Button(
            onClick = {
                checkValidity()
                if (!valid) return@Button

                if (!isNewHabit && habit.frequency != oldHabit.frequency) {
                    showAlertDialog = true
                } else {
                    saveHabit()
                }
            },
            enabled = valid,
            modifier = Modifier.width(100.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Save", style = MaterialTheme.typography.titleMedium)

            BasicAlertDialog(
                showAlertDialog = showAlertDialog,
                icon = Icons.Default.Warning,
                title = "Warning",
                text = "Changing the frequency will reset completed tasks. Are you sure?",
                onDismissRequest = { showAlertDialog = false }
            ) {
                showAlertDialog = false
                saveHabit()
            }
        }
    }
}

@Composable
fun TitleTextField(habit: Habit, modifier: Modifier = Modifier, checkValidity: () -> Unit) {
    BoxWithConstraints {
        val boxWithConstraintsScope = this
        ExpandingTextField(
            initialText = habit.title,
            hint = "Enter title...",
            modifier = modifier,
            width = boxWithConstraintsScope.maxWidth - 44.dp,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            showHintIfEmpty = true
        ) {
            habit.title = it
            checkValidity()
        }
    }
}

@Composable
fun DescriptionTextField(habit: Habit, modifier: Modifier = Modifier, checkValidity: () -> Unit) {
    var description by remember { mutableStateOf(habit.description) }

    OutlinedTextField(
        value = description,
        onValueChange = { description = it; habit.description = it; checkValidity() },
        label = { Text("Description") },
        minLines = 2,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )
}

@Composable
fun CategoryTextField(userDataViewModel: UserDataViewModel, navController: NavController, habit: Habit, modifier: Modifier = Modifier, checkValidity: () -> Unit) {
    CustomDropdownSelector(
        options = userDataViewModel.categories,
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyLarge,
        initialOption = if (habit.category == Category.NONE) "Select a category" else habit.category,
        userDataViewModel = userDataViewModel,
        navController = navController
    ) {
        habit.category = it
        checkValidity()
    }
}

@Composable
fun FrequencySelector(userDataViewModel: UserDataViewModel, habit: Habit, modifier: Modifier = Modifier, onFrequencyChange: (String) -> Unit = {}) {
    OptionsRow(userDataViewModel.frequencyTypes, modifier, habit.frequency) { onFrequencyChange(it) }
}

@Composable
fun TimeSelector(habit: Habit, modifier: Modifier = Modifier, checkValidity: () -> Unit) {
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
                    checkValidity()
                }
            ) {
                Icon(Icons.Default.Add, "Add")
            }
        }

        LazyColumn {
            items(startTimes.size) { index ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier.fillMaxWidth().padding(start = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            startTimes[index].format(DateTimeFormatter.ofPattern("hh:mm a")),
                            color = if (startTimes[index].isAfter(endTimes[index])) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                        TimePickerButton(
                            time = startTimes[index]
                        ) { _, hour, minute ->
                            startTimes[index] = LocalTime.of(hour, minute)
                            habit.taskList.startTimes[index] = startTimes[index]
                            checkValidity()
                        }

                        Text("to")
                        Text(
                            endTimes[index].format(DateTimeFormatter.ofPattern("hh:mm a")),
                            color = if (startTimes[index].isAfter(endTimes[index])) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                        TimePickerButton(
                            time = endTimes[index]
                        ) { _, hour, minute ->
                            endTimes[index] = LocalTime.of(hour, minute)
                            habit.taskList.endTimes[index] = endTimes[index]
                            checkValidity()
                        }

                        IconButton(
                            onClick = {
                                startTimes.removeAt(index)
                                endTimes.removeAt(index)
                                habit.taskList.startTimes.removeAt(index)
                                habit.taskList.endTimes.removeAt(index)
                                checkValidity()
                            }
                        ) {
                            Icon(Icons.Default.Delete, "Delete")
                        }
                    }

                    if (startTimes[index].isAfter(endTimes[index])) {
                        Text("Start time cannot be after end time", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun DayOfWeekSelector(habit: Habit, modifier: Modifier = Modifier, checkValidity: () -> Unit) {
    val selectedDays = remember { mutableStateMapOf<Int, Boolean>().apply { habit.taskList.daysOfWeek.forEach { put(it.key, it.value) } } }

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
                        if (selectedDays[day] == true) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable {
                        if (selectedDays[day] == true) {
                            selectedDays[day] = false
                            habit.taskList.daysOfWeek[day] = false
                        } else {
                            selectedDays[day] = true
                            habit.taskList.daysOfWeek[day] = true
                        }
                        checkValidity()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = Week.getDayName(day).first().toString(),
                    color = if (selectedDays[day] == true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
fun DayOfMonthSelector(habit: Habit, modifier: Modifier = Modifier, checkValidity: () -> Unit) {
    val selectedDays = remember { mutableStateMapOf<Int, Boolean>().apply { habit.taskList.daysOfWeek.forEach { put(it.key, it.value) } } }

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
                                if (selectedDays[day] == true) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable {
                                if (selectedDays[day] == true) {
                                    selectedDays[day] = false
                                    habit.taskList.daysOfMonth[day] = false
                                } else {
                                    selectedDays[day] = true
                                    habit.taskList.daysOfMonth[day] = true
                                }
                                checkValidity()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            color = if (selectedDays[day] == true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                }
            }
        }
    }
}

@Composable
fun DateSelector(habit: Habit, oldHabit: Habit, isNewHabit: Boolean, modifier: Modifier = Modifier, checkValidity: () -> Unit) {
    val context = LocalContext.current
    var startDate by remember { mutableStateOf(habit.taskList.startDate) }
    var endDate by remember { mutableStateOf(habit.taskList.endDate) }

    val onStartDateClick = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                startDate = LocalDate.of(year, month + 1, dayOfMonth)
                habit.taskList.startDate = startDate
                checkValidity()
            },
            startDate.year,
            startDate.month.ordinal,
            startDate.dayOfMonth
        ).show()
    }

    val onEndDateClick = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                endDate = LocalDate.of(year, month + 1, dayOfMonth)
                habit.taskList.endDate = endDate
                checkValidity()
            },
            endDate.year,
            endDate.month.ordinal,
            endDate.dayOfMonth
        ).show()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                startDate.format(DateTimeFormatter.ofPattern("d MMM")),
                modifier = Modifier.clickable(onClick = onStartDateClick),
                color = if (startDate.isAfter(endDate)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                onClick = { onStartDateClick() }
            ) {
                Icon(Icons.Default.DateRange, "Date")
            }
            Text("to", modifier = Modifier.padding(end = 12.dp))

            Text(
                endDate.format(DateTimeFormatter.ofPattern("d MMM")),
                modifier = Modifier.clickable(onClick = onEndDateClick),
                color = if (startDate.isAfter(endDate)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                onClick = { onEndDateClick() }
            ) {
                Icon(Icons.Default.DateRange, "Date")
            }
        }

        if (startDate.isAfter(endDate)) {
            Text("Start date cannot be after end date", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        }

        if (startDate.isBefore(LocalDate.now())) {
            Text("Start date cannot be in the past", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        }

        if (!isNewHabit && startDate != oldHabit.taskList.startDate) {
            Text("Start date cannot be edited", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        }
    }
}
