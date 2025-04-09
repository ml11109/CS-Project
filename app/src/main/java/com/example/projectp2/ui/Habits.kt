package com.example.projectp2.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.BasicAlertDialog
import com.example.projectp2.composables.BasicExpandingSearchBar
import com.example.projectp2.composables.DatePickerSwitch
import com.example.projectp2.composables.DropdownTextBox
import com.example.projectp2.composables.FadeColumn
import com.example.projectp2.composables.FadeRow
import com.example.projectp2.composables.OptionsRow
import com.example.projectp2.composables.PagerSwitcher
import com.example.projectp2.composables.WeekSelector
import com.example.projectp2.model.Category
import com.example.projectp2.model.Filter
import com.example.projectp2.model.Frequency
import com.example.projectp2.model.Habit
import com.example.projectp2.model.HabitCard
import com.example.projectp2.model.SimpleTaskCard
import com.example.projectp2.model.HabitStatus
import com.example.projectp2.model.Task
import com.example.projectp2.model.TaskCard
import com.example.projectp2.model.TaskCompletionDialog
import com.example.projectp2.model.TaskStatus
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Week
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate

@Composable
fun HabitsScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    val boxModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
    val focusManager = LocalFocusManager.current
    var filter by remember { mutableStateOf(Filter()) }

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
                    FilterOptions(userDataViewModel, filter, Modifier.fillMaxWidth().height(30.dp)) { filter = it }
                    Spacer(Modifier.height(8.dp))

                    TaskScreen(userDataViewModel, filter, boxModifier.fillMaxWidth().height(maxHeight.times(0.4f)))
                    HorizontalDivider(Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 4.dp))

                    if (filter.filterHabits(userDataViewModel.habits).isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Info, "Info", modifier = Modifier.size(36.dp).alpha(0.5f))
                            Text("No habits found", style = MaterialTheme.typography.titleMedium, modifier = Modifier.alpha(0.5f).padding(top = 16.dp, bottom = 48.dp))
                        }
                    } else {
                        HabitList(userDataViewModel, navController, filter, Modifier.fillMaxWidth().weight(1f).padding(horizontal = 12.dp))
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
                            FilterOptions(userDataViewModel, filter, Modifier.fillMaxWidth().height(30.dp)) { filter = it }
                            Spacer(Modifier.height(8.dp))

                            TaskScreen(userDataViewModel, filter, boxModifier.fillMaxSize())
                        }
                        Spacer(Modifier.width(12.dp))

                        if (filter.filterHabits(userDataViewModel.habits).isEmpty()) {
                            Column(
                                modifier = Modifier.height(contentHeight).weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Info, "Info", modifier = Modifier.size(36.dp).alpha(0.5f))
                                Text("No habits found", style = MaterialTheme.typography.titleMedium, modifier = Modifier.alpha(0.5f).padding(top = 16.dp))
                            }
                        } else {
                            HabitList(userDataViewModel, navController, filter, Modifier.height(contentHeight).weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterOptions(userDataViewModel: UserDataViewModel, filter: Filter, modifier: Modifier = Modifier, onFilterChange: (filter: Filter) -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FadeRow(
            backgroundColor = MaterialTheme.colorScheme.background,
            fadeWidth = 8.dp,
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dropdownModifier = Modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp))
                val textStyle = MaterialTheme.typography.bodySmall

                DropdownTextBox(
                    arrayListOf(HabitStatus.ALL) + userDataViewModel.habitStatusTypes,
                    "Status", dropdownModifier, textStyle,
                ) {
                    onFilterChange(filter.copy(status = it))
                }

                DropdownTextBox(
                    arrayListOf(Category.ALL) + userDataViewModel.categories,
                    "Category", dropdownModifier, textStyle,
                ) {
                    onFilterChange(filter.copy(category = it))
                }

                DropdownTextBox(
                    arrayListOf(Frequency.ALL) + userDataViewModel.frequencyTypes,
                    "Frequency", dropdownModifier, textStyle,
                ) {
                    onFilterChange(filter.copy(frequency = it))
                }
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
                onFilterChange(filter.copy(title = it))
            }

            DatePickerSwitch(
                date = filter.date,
                modifier = Modifier.width(40.dp).height(30.dp),
                on = filter.filterDate,
                onDateSelect = { _, year, month, day ->
                    filter.date = LocalDate.of(year, month + 1, day)
                    filter.filterDate = true
                    onFilterChange(filter)
                },
                onSwitchOff = {
                    onFilterChange(filter.copy(filterDate = false))
                }
            )
        }
    }
}

@Composable
fun TaskScreen(userDataViewModel: UserDataViewModel, filter: Filter, modifier: Modifier = Modifier) {
    val screenModifier = Modifier.fillMaxSize().padding(16.dp)

    PagerSwitcher(2, modifier) {
        when (it) {
            1, 3 -> TaskColumn(userDataViewModel, filter, screenModifier)
            2, 0 -> TaskCalendar(userDataViewModel, filter, screenModifier)
        }
    }
}

@Composable
fun TaskColumn(userDataViewModel: UserDataViewModel, filter: Filter, modifier: Modifier = Modifier) {
    val tasks = arrayListOf<Task>()
    var status by remember { mutableStateOf(TaskStatus.ONGOING) }
    var dialogShown by remember { mutableStateOf(false) }
    var selectedTaskIndex by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    fun updateTasks() {
        tasks.clear()
        tasks.addAll(
            when (status) {
                TaskStatus.ALL -> userDataViewModel.getAllTasks(filter)
                TaskStatus.ONGOING -> userDataViewModel.getOngoingTasks(filter)
                TaskStatus.UPCOMING -> userDataViewModel.getUpcomingTasks(filter)
                TaskStatus.COMPLETED -> userDataViewModel.getCompletedTasks(filter)
                TaskStatus.FAILED -> userDataViewModel.getFailedTasks(filter)
                else -> userDataViewModel.getAllTasks(filter)
            }
        )
    }
    updateTasks()

    Column(
        modifier = modifier
    ) {
        OptionsRow(
            options = userDataViewModel.taskStatusTypes + arrayListOf(TaskStatus.ALL),
            initialOption = TaskStatus.ONGOING,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth().height(30.dp)
        ) {
            status = it
            updateTasks()
        }
        Spacer(Modifier.height(8.dp))

        if (tasks.isNotEmpty()) {
            FadeColumn(
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                fadeHeight = 8.dp,
                modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(Modifier) }

                    items(tasks.size) { index ->
                        val task = tasks[index]
                        TaskCard(
                            userDataViewModel,
                            task,
                            Modifier.fillMaxWidth().clickable {
                                selectedTaskIndex = index
                                dialogShown = true
                            }
                        )
                    }
                    item { Spacer(Modifier) }
                }
            }
        }

        else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No ${status.lowercase()} tasks found",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.alpha(0.5f)
                )
            }
        }

        if (dialogShown) {
            TaskCompletionDialog(
                userDataViewModel,
                tasks[selectedTaskIndex]
            ) {
                updateTasks()
                dialogShown = false
            }
        }
    }
}

@Composable
fun TaskCalendar(userDataViewModel: UserDataViewModel, filter: Filter, modifier: Modifier = Modifier) {
    val tasks = arrayListOf<List<Task>>().apply { repeat(7) { add(listOf()) } }
    var firstDayOfWeek by remember { mutableStateOf(LocalDate.now().minusDays(LocalDate.now().dayOfWeek.ordinal.toLong())) }

    fun setTasks(date: LocalDate) {
        for (dayOfWeek in 0 until 7) {
            tasks[dayOfWeek] = userDataViewModel.getTasks(filter) { it.date == date.plusDays(dayOfWeek.toLong()) }.sortedBy { it.startTime }
        }
    }
    setTasks(firstDayOfWeek)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeekSelector(firstDayOfWeek, Modifier.padding(bottom = 4.dp)) { firstDayOfWeek = it }

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
fun HabitList(userDataViewModel: UserDataViewModel, navController: NavController, filter: Filter, modifier: Modifier = Modifier) {
    val habits = remember { mutableStateListOf<Habit>().apply { addAll(filter.filterHabits(userDataViewModel.habits)) } }
    var showAlertDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    FadeColumn(
        backgroundColor = MaterialTheme.colorScheme.background,
        fadeHeight = 8.dp,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(Modifier) }

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
                        showAlertDialog = true
                    }
                )
            }
            item { Spacer(Modifier) }
        }
    }
    
    BasicAlertDialog(
        showAlertDialog = showAlertDialog,
        Icons.Default.Warning,
        "Delete Habit",
        "Are you sure you want to delete this habit?",
        onDismissRequest = { showAlertDialog = false }
    ) {
        showAlertDialog = false
        userDataViewModel.habits.remove(habits[selectedIndex])
        habits.removeAt(selectedIndex)
        userDataViewModel.updateHabitCompletion(context)
        userDataViewModel.saveHabits(context)
        Toast.makeText(context, "Habit deleted", Toast.LENGTH_SHORT).show()
    }
}
