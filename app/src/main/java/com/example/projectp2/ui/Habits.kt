package com.example.projectp2.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.projectp2.composables.OptionsRow
import com.example.projectp2.composables.ScreenSwitcher
import com.example.projectp2.model.Category
import com.example.projectp2.model.Filter
import com.example.projectp2.model.Frequency
import com.example.projectp2.model.Habit
import com.example.projectp2.model.HabitCard
import com.example.projectp2.model.SimpleTaskCard
import com.example.projectp2.model.Status
import com.example.projectp2.model.Task
import com.example.projectp2.model.TaskCard
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Week
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

                    TaskScreen(userDataViewModel, boxModifier.fillMaxWidth().height(maxHeight.times(0.4f)))
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

                            TaskScreen(userDataViewModel, boxModifier.fillMaxSize())
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
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val dropdownModifier = Modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp))
            val textStyle = MaterialTheme.typography.bodySmall

            DropdownTextBox(
                arrayListOf(Status.ALL) + userDataViewModel.statusTypes,
                "Status", dropdownModifier, textStyle,
            ) {
                filter.status = it
                onFilterChange()
            }

            DropdownTextBox(
                arrayListOf(Category.ALL) + userDataViewModel.categories,
                "Category", dropdownModifier, textStyle,
            ) {
                filter.category = it
                onFilterChange()
            }

            DropdownTextBox(
                arrayListOf(Frequency.ALL) + userDataViewModel.frequencyTypes,
                "Frequency", dropdownModifier, textStyle,
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
fun TaskScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    ScreenSwitcher(2, 0, modifier) {
        when (it) {
            0 -> TaskColumn(userDataViewModel, modifier = Modifier.fillMaxSize().padding(16.dp))
            1 -> TaskCalendar(userDataViewModel, modifier = Modifier.fillMaxSize().padding(16.dp))
        }
    }
}

@Composable
fun TaskColumn(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    var taskStatus by remember { mutableStateOf(Status.ALL) }
    val tasks = remember { mutableStateListOf<Task>().apply { addAll(userDataViewModel.getAllTasks()) } }

    Column(
        modifier = modifier
    ) {
        OptionsRow(
            options = arrayListOf(Status.ALL) + userDataViewModel.statusTypes,
            initialOption = Status.ALL,
            modifier = Modifier.fillMaxWidth().height(30.dp)
        ) {
            taskStatus = it
            tasks.clear()

            tasks.addAll(
                when (taskStatus) {
                    Status.ALL -> userDataViewModel.getAllTasks()
                    Status.ONGOING -> userDataViewModel.getOngoingTasks(100)
                    Status.UPCOMING -> userDataViewModel.getUpcomingTasks(100)
                    Status.NOT_COMPLETED -> userDataViewModel.getNotCompletedTasks(100)
                    Status.COMPLETED -> userDataViewModel.getCompletedTasks(100)
                    else -> userDataViewModel.getAllTasks()
                }
            )
        }
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks.size) { index ->
                TaskCard(
                    userDataViewModel,
                    tasks[index],
                    Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun TaskCalendar(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    var firstDayOfWeek by remember { mutableStateOf(LocalDate.now().minusDays(LocalDate.now().dayOfWeek.ordinal.toLong())) }
    val tasks = remember { mutableStateListOf<List<Task>>().apply { repeat(7) { add(listOf()) } } }

    fun setTasks(date: LocalDate) {
        for (dayOfWeek in 0 until 7) {
            tasks[dayOfWeek] = userDataViewModel.getTasks { it.date == date.plusDays(dayOfWeek.toLong()) }.sortedBy { it.startTime }
        }
    }
    setTasks(firstDayOfWeek)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    firstDayOfWeek = firstDayOfWeek.minusWeeks(1)
                    setTasks(firstDayOfWeek)
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous")
            }

            Text(firstDayOfWeek.format(DateTimeFormatter.ofPattern("d/MM")) + " - " +
                        firstDayOfWeek.plusDays(6).format(DateTimeFormatter.ofPattern("d/MM")))

            IconButton(
                onClick = {
                    firstDayOfWeek = firstDayOfWeek.plusWeeks(1)
                    setTasks(firstDayOfWeek)
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
            }
        }

        Row(
            modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState())
        ) {
            for (dayOfWeek in 0 until 7) {
                Column(
                    modifier = Modifier.fillMaxHeight().width(100.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(30.dp).border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(Week.getDayName(dayOfWeek + 1), style = MaterialTheme.typography.bodySmall)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f).border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)).padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tasks[dayOfWeek].size) { index ->
                            SimpleTaskCard(
                                userDataViewModel,
                                tasks[dayOfWeek][index],
                                Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
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
