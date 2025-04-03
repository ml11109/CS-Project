package com.example.projectp2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.BasicExpandingSearchBar
import com.example.projectp2.composables.DatePickerSwitch
import com.example.projectp2.composables.DropdownTextBox
import com.example.projectp2.composables.ScreenSwitcher
import com.example.projectp2.model.Filter
import com.example.projectp2.model.Frequency
import com.example.projectp2.model.Habit
import com.example.projectp2.model.Task
import com.example.projectp2.model.UserDataViewModel
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HabitsScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope, filter: Filter = Filter()) {
    val boxModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
    val focusManager = LocalFocusManager.current

    AppScaffold(
        title = "Habits",
        navController = navController,
        drawerState = drawerState,
        scope = scope,
        floatingActionButton = { AddNewFAB(navController) }
    ) { nestedScrollConnection ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                }
        ) {
            FilterOptions(userDataViewModel, filter, Modifier.height(30.dp))
            Spacer(Modifier.height(12.dp))

            HabitCalendar(boxModifier.fillMaxWidth().height(300.dp))
            HorizontalDivider(Modifier.fillMaxWidth().padding(12.dp))

            HabitList(userDataViewModel, Filter.filterHabits(userDataViewModel.habits), Modifier.fillMaxWidth().weight(1f))
            // TODO: Placeholder for empty list
        }
    }
}

@Composable
fun MiniHabitsScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Habits")
    }
}

@Composable
fun FilterOptions(userDataViewModel: UserDataViewModel, filter: Filter, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier.weight(1f).horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DropdownTextBox(
                listOf("All", "Ongoing", "Completed"),
                modifier = modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Status"
            ) {
                filter.status = it
            }
            Spacer(modifier.width(8.dp))

            DropdownTextBox(
                userDataViewModel.categories,
                modifier = modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Category"
            ) {
                filter.category = it
            }
            Spacer(modifier.width(8.dp))

            DropdownTextBox(
                userDataViewModel.frequencyTypes,
                modifier = modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Frequency"
            ) {
                filter.frequency = it
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicExpandingSearchBar(
                modifier = modifier.padding(start = 8.dp, end = 4.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                height = 24.dp
            ) {
                filter.title = it
            }

            DatePickerSwitch(
                date = filter.date,
                modifier = modifier,
                on = filter.filterDate,
                onDateSelect = { _, year, month, day ->
                    filter.date = LocalDate.of(year, month, day)
                },
                onSwitchOff = {
                    filter.filterDate = false
                }
            )
        }
    }
}

@Composable
fun HabitCalendar(modifier: Modifier = Modifier) {
    ScreenSwitcher(3, 1, modifier) { screen ->
        val screenModifier = Modifier.fillMaxSize().padding(16.dp)
        when (screen) {
            0 -> HabitCalendarMonth(screenModifier)
            1 -> HabitCalendarWeek(screenModifier)
            2 -> HabitCalendarDay(screenModifier)
        }
    }
}

@Composable
fun HabitCalendarMonth(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Month")
    }
}

@Composable
fun HabitCalendarWeek(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Week")
    }
}

@Composable
fun HabitCalendarDay(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Day")
    }
}

@Composable
fun HabitList(userDataViewModel: UserDataViewModel, habits: ArrayList<Habit>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(habits.size) { index ->
                HabitCard(
                    userDataViewModel,
                    habits[index],
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun HabitCard(userDataViewModel: UserDataViewModel, habit: Habit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
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
                    .background(userDataViewModel.getCategoryColor(habit.category))
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = habit.frequency,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = when (habit.frequency) {
                            Frequency.DAILY -> "${habit.taskList.startTimes.size} times a day"
                            Frequency.WEEKLY -> "${habit.taskList.daysOfWeek.values.count { it }} times a week"
                            Frequency.MONTHLY -> "${habit.taskList.daysOfMonth.values.count { it }} times a month"
                            else -> "Never"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LinearProgressIndicator(
                    progress = { habit.taskList.getProgress() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = userDataViewModel.getCategoryColor(habit.category),
                )
            }
        }
    }
}
