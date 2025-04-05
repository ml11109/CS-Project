package com.example.projectp2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.projectp2.model.HabitCard
import com.example.projectp2.model.Task
import com.example.projectp2.model.UserDataViewModel
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HabitsScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    val boxModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
    val focusManager = LocalFocusManager.current
    val filter = Filter()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val maxHeight = this.maxHeight

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

                TaskList(boxModifier.fillMaxWidth().height(maxHeight.times(0.35f)))
                HorizontalDivider(Modifier.fillMaxWidth().padding(12.dp))

                HabitList(userDataViewModel, navController, Filter.filterHabits(userDataViewModel.habits), Modifier.fillMaxWidth().weight(1f))
                // TODO: Placeholder for empty list
            }
        }
    }
}

@Composable
fun MiniTasksScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Tasks")
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
fun TaskList(modifier: Modifier = Modifier) {
    ScreenSwitcher(2, 0, modifier) { screen ->
        val screenModifier = Modifier.fillMaxSize().padding(16.dp)
        when (screen) {
            0 -> HabitCalendarDay(screenModifier)
            1 -> HabitCalendarWeek(screenModifier)
        }
    }
}

@Composable
fun HabitCalendarDay(modifier: Modifier = Modifier) {
    /*
    Column of detailed task cards (sort options maybe? otherwise sort by start time)
    - Ongoing tasks
     */

    Box(modifier = modifier) {
        Text("Day")
    }
}

@Composable
fun HabitCalendarWeek(modifier: Modifier = Modifier) {
    /*
    Row of columns of small task cards
     */

    Box(modifier = modifier) {
        Text("Week")
    }
}

@Composable
fun HabitList(userDataViewModel: UserDataViewModel, navController: NavController, habits: ArrayList<Habit>, modifier: Modifier = Modifier) {
    val habitStateList = remember { mutableStateListOf<Habit>().apply { addAll(habits) } }

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(habitStateList.size) { index ->
                HabitCard(
                    userDataViewModel,
                    habitStateList[index],
                    Modifier.fillMaxWidth(),
                    onHabitSelect = {
                        // TODO: Set filter to only show this one
                    },
                    onHabitEdit = {
                        navController.navigate("details/1/${userDataViewModel.habits.indexOf(habits[index])}")
                    },
                    onHabitCopy = {
                        navController.navigate("details/2/${userDataViewModel.habits.indexOf(habits[index])}")
                    },
                    onHabitDelete = {
                        userDataViewModel.habits.remove(habits[index])
                        habitStateList.removeAt(index)
                    }
                )
            }
        }
    }
}
