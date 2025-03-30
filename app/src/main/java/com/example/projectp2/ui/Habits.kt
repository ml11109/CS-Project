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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.BasicExpandingSearchBar
import com.example.projectp2.composables.DatePickerButton
import com.example.projectp2.composables.DropdownTextBox
import com.example.projectp2.composables.ScreenSwitcher
import com.example.projectp2.model.FilterViewModel
import com.example.projectp2.model.Habit
import com.example.projectp2.model.Task
import com.example.projectp2.model.UserDataViewModel

@Composable
fun HabitsScreen(userDataViewModel: UserDataViewModel, navController: NavController) {
    val boxModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
    val filterViewModel: FilterViewModel = viewModel()
    val focusManager = LocalFocusManager.current

    AppScaffold(
        title = "Habits",
        navController = navController,
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
            FilterOptions(userDataViewModel, filterViewModel, Modifier.height(30.dp))
            Spacer(Modifier.height(12.dp))

            HabitCalendar(boxModifier.fillMaxWidth().height(250.dp))
            Spacer(Modifier.height(12.dp))

            HorizontalDivider(Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))

            HabitList(filterViewModel.filterHabits(userDataViewModel.habits), Modifier.fillMaxWidth().weight(1f))
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
fun FilterOptions(userDataViewModel: UserDataViewModel, filterViewModel: FilterViewModel, modifier: Modifier = Modifier) {
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
            )
            Spacer(modifier.width(8.dp))

            DropdownTextBox(
                userDataViewModel.categories,
                modifier = modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Category"
            )
            Spacer(modifier.width(8.dp))

            DropdownTextBox(
                userDataViewModel.frequencyTypes,
                modifier = modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Frequency"
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicExpandingSearchBar(
                modifier = modifier.padding(start = 8.dp, end = 4.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                height = 24.dp
            ) {
                filterViewModel.filter.title = it
            }

            DatePickerButton(filterViewModel.calendar, modifier) { _, year, month, day ->
                filterViewModel.calendar.set(year, month, day)
            }
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
fun HabitList(habits: MutableList<Habit>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(habits.size) { index ->
                HabitCard(
                    habits[index],
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp) // Temp
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {}
}

@Composable
fun HabitCard(habit: Habit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {}
}
