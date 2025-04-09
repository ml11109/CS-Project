package com.example.projectp2.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.R
import com.example.projectp2.composables.ScreenSwitcher
import com.example.projectp2.model.Filter
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Task
import com.example.projectp2.model.TaskCard
import com.example.projectp2.model.TaskCompletionDialog
import com.example.projectp2.util.RequestNotificationPermission
import kotlinx.coroutines.CoroutineScope
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    val boxModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))

    val upcomingTasks = remember {
        mutableStateListOf<Task>().apply {
            addAll(userDataViewModel.getUpcomingTasks())
        }
    }
    val completedTasks = remember {
        mutableStateListOf<Task>().apply {
            addAll(userDataViewModel.getCompletedTasks())
        }
    }

    fun updateTasks() {
        completedTasks.clear()
        completedTasks.addAll(userDataViewModel.getCompletedTasks().take(10))
        upcomingTasks.clear()
        upcomingTasks.addAll(userDataViewModel.getUpcomingTasks().take(10))
    }

    RequestNotificationPermission {}

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val maxHeight = this.maxHeight

        AppScaffold(
            title = stringResource(R.string.app_name),
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
                ) {
                    InfoBar(userDataViewModel, navController, Modifier.fillMaxWidth().height(50.dp))
                    Spacer(Modifier.height(12.dp))

                    MiniScreen(
                        userDataViewModel,
                        boxModifier.fillMaxWidth().height(maxHeight.times(0.35f))
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth().weight(1f)) {

                        MiniTaskColumn(
                            "Upcoming", upcomingTasks,
                            userDataViewModel, navController,
                            boxModifier.fillMaxHeight().weight(1f)
                        ) { updateTasks() }
                        Spacer(Modifier.width(8.dp))

                        MiniTaskColumn(
                            "Completed", completedTasks,
                            userDataViewModel, navController,
                            boxModifier.fillMaxHeight().weight(1f)
                        ) { updateTasks() }
                    }
                }
            }

            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .nestedScroll(nestedScrollConnection)
                        .verticalScroll(rememberScrollState())
                ) {
                    InfoBar(userDataViewModel, navController, Modifier.fillMaxWidth().height(50.dp))
                    Spacer(Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        MiniScreen(
                            userDataViewModel,
                            boxModifier.fillMaxHeight().weight(2f)
                        )
                        Spacer(Modifier.width(8.dp))

                        MiniTaskColumn(
                            "Upcoming", upcomingTasks,
                            userDataViewModel, navController,
                            boxModifier.fillMaxHeight().weight(1f)
                        ) { updateTasks() }
                        Spacer(Modifier.width(8.dp))

                        MiniTaskColumn(
                            "Completed", completedTasks,
                            userDataViewModel, navController,
                            boxModifier.fillMaxHeight().weight(1f)
                        ) { updateTasks() }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBar(userDataViewModel: UserDataViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val ongoingTasks = userDataViewModel.getTasks { it.isOngoing() }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            when (ongoingTasks.size) {
                0 -> Text(
                    text = "Welcome back!", // TODO: add info text
                    style = MaterialTheme.typography.titleMedium
                )

                1 -> Text(
                    text = "Ongoing: ${userDataViewModel.getHabitFromId(ongoingTasks.first().habitId).title} " +
                            "by ${ongoingTasks.first().endTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                    style = MaterialTheme.typography.titleMedium
                )

                else -> Row {
                    Text(
                        text = "You have ${ongoingTasks.size} ongoing tasks",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Click to view ->",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.clickable {
                            navController.navigate("habits/")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MiniScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    ScreenSwitcher(3, 0, modifier) { screenNum ->
        val screenModifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)
        when (screenNum) {
            0 -> TaskColumn(userDataViewModel, Filter(), screenModifier)
            1 -> MiniStatsScreen(userDataViewModel, screenModifier)
            2 -> MiniAchievementsScreen(userDataViewModel, screenModifier)
        }
    }
}

@Composable
fun MiniStatsScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Stats")
    }
}

@Composable
fun MiniAchievementsScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Achievements")
    }
}

@Composable
fun MiniTaskColumn(title: String, tasks: SnapshotStateList<Task>, userDataViewModel: UserDataViewModel, navController: NavController,
                   modifier: Modifier = Modifier, updateTasks: () -> Unit) {
    var dialogShown by remember { mutableStateOf(false) }
    var selectedTaskIndex by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$title:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(tasks.size) { index ->
                val task = tasks[index]
                TaskCard(
                    userDataViewModel,
                    task,
                    Modifier.fillMaxWidth().clickable {
                        if (task.isCompletable(userDataViewModel)) {
                            selectedTaskIndex = index
                            dialogShown = true
                        }
                    }
                )
            }

            if (tasks.isNotEmpty()) {
                item {
                    Text(
                        text = "See more",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp).clickable { navController.navigate("habits") }
                    )
                }
            }
        }

        if (dialogShown) {
            TaskCompletionDialog(
                userDataViewModel,
                tasks[selectedTaskIndex],
                onDismiss = {
                    updateTasks()
                    dialogShown = false
                },
            ) {
                userDataViewModel.saveHabits(context)
                Toast.makeText(context, "Task details saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
