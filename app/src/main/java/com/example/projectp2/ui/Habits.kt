package com.example.projectp2.ui

import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.BasicExpandingSearchBar
import com.example.projectp2.composables.DatePickerSwitch
import com.example.projectp2.composables.DropdownTextBox
import com.example.projectp2.composables.ScreenSwitcher
import com.example.projectp2.model.Category
import com.example.projectp2.model.Filter
import com.example.projectp2.model.Frequency
import com.example.projectp2.model.Habit
import com.example.projectp2.model.HabitCard
import com.example.projectp2.model.Status
import com.example.projectp2.model.UserDataViewModel
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate

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
            val filteredHabits = remember { mutableStateListOf<Habit>().apply { addAll(filter.filterHabits(userDataViewModel.habits)) } }

            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
                    FilterOptions(userDataViewModel, filter, Modifier.fillMaxWidth().height(30.dp)) {
                        filteredHabits.clear(); filteredHabits.addAll(filter.filterHabits(userDataViewModel.habits))
                    }
                    Spacer(Modifier.height(8.dp))

                    TaskList(boxModifier.fillMaxWidth().height(maxHeight.times(0.35f)))
                    HorizontalDivider(Modifier.fillMaxWidth().padding(12.dp))

                    if (filteredHabits.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Info, "Info", modifier = Modifier.size(36.dp).alpha(0.5f))
                            Text("No habits found", style = MaterialTheme.typography.titleMedium, modifier = Modifier.alpha(0.5f).padding(top = 16.dp, bottom = 48.dp))
                        }
                    } else {
                        HabitList(userDataViewModel, navController, filteredHabits, Modifier.fillMaxWidth().weight(1f))
                    }
                }
            }

            else {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val contentHeight = this.maxHeight - 32.dp

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .nestedScroll(nestedScrollConnection)
                            .verticalScroll(rememberScrollState())
                            .pointerInput(Unit) {
                                detectTapGestures { focusManager.clearFocus() }
                            }
                    ) {
                        Column(
                            modifier = Modifier.height(contentHeight).weight(1f)
                        ) {
                            FilterOptions(userDataViewModel, filter, Modifier.fillMaxWidth().height(30.dp)) {
                                filteredHabits.clear(); filteredHabits.addAll(filter.filterHabits(userDataViewModel.habits))
                            }
                            Spacer(Modifier.height(8.dp))

                            TaskList(boxModifier.fillMaxSize())
                        }
                        Spacer(Modifier.width(12.dp))

                        if (filteredHabits.isEmpty()) {
                            Column(
                                modifier = Modifier.height(contentHeight).weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Info, "Info", modifier = Modifier.size(36.dp).alpha(0.5f))
                                Text("No habits found", style = MaterialTheme.typography.titleMedium, modifier = Modifier.alpha(0.5f).padding(top = 16.dp))
                            }
                        } else {
                            HabitList(userDataViewModel, navController, filteredHabits, Modifier.height(contentHeight).weight(1f))
                        }
                    }
                }
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
fun FilterOptions(userDataViewModel: UserDataViewModel, filter: Filter, modifier: Modifier = Modifier, onFilterChange: () -> Unit) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f).horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DropdownTextBox(
                arrayListOf(Status.ALL) + userDataViewModel.statusTypes,
                modifier = Modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Status"
            ) {
                filter.status = it
                onFilterChange()
            }
            Spacer(Modifier.width(8.dp))

            DropdownTextBox(
                arrayListOf(Category.ALL) + userDataViewModel.categories,
                modifier = Modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Category"
            ) {
                filter.category = it
                onFilterChange()
            }
            Spacer(Modifier.width(8.dp))

            DropdownTextBox(
                arrayListOf(Frequency.ALL) + userDataViewModel.frequencyTypes,
                modifier = Modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Frequency"
            ) {
                filter.frequency = it
                onFilterChange()
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicExpandingSearchBar(
                modifier = Modifier.padding(start = 8.dp, end = 4.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                height = 24.dp
            ) {
                filter.title = it
                onFilterChange()
            }

            DatePickerSwitch(
                date = filter.date,
                modifier = Modifier.width(40.dp).height(30.dp),
                on = filter.filterDate,
                onDateSelect = { _, year, month, day ->
                    filter.date = LocalDate.of(year, month + 1, day)
                    filter.filterDate = true
                    onFilterChange()
                },
                onSwitchOff = {
                    filter.filterDate = false
                    onFilterChange()
                }
            )
        }
    }
}

@Composable
fun TaskList(modifier: Modifier = Modifier) {
    ScreenSwitcher(3, 0, modifier) { screen ->
        val screenModifier = Modifier.fillMaxSize().padding(16.dp)
        when (screen) {
            0 -> HabitOverview(screenModifier)
            1 -> HabitCalendarDay(screenModifier)
            2 -> HabitCalendarWeek(screenModifier)
        }
    }
}

@Composable
fun HabitOverview(modifier: Modifier = Modifier) {

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
fun HabitList(userDataViewModel: UserDataViewModel, navController: NavController, habits: SnapshotStateList<Habit>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(habits.size) { index ->
                HabitCard(
                    userDataViewModel,
                    habits[index],
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
                        habits.removeAt(index)
                    }
                )
            }
        }
    }
}
