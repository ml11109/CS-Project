package com.example.projectp2.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.R
import com.example.projectp2.composables.ScreenSwitcher
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Task
import com.example.projectp2.model.TaskCard
import kotlinx.coroutines.CoroutineScope
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    val boxModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))

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
                        MiniTaskList(
                            "Completed", userDataViewModel.getCompletedTasks(5),
                            userDataViewModel, navController,
                            boxModifier.fillMaxHeight().weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))

                        MiniTaskList(
                            "Upcoming", userDataViewModel.getUpcomingTasks(5),
                            userDataViewModel, navController,
                            boxModifier.fillMaxHeight().weight(1f)
                        )
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

                        MiniTaskList(
                            "Completed", userDataViewModel.getCompletedTasks(5),
                            userDataViewModel, navController,
                            boxModifier.fillMaxHeight().weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))

                        MiniTaskList(
                            "Upcoming", userDataViewModel.getUpcomingTasks(5),
                            userDataViewModel, navController,
                            boxModifier.fillMaxHeight().weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBar(userDataViewModel: UserDataViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val ongoingTasks = userDataViewModel.getOngoingTasks()

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
                    text = "Ongoing: ${ongoingTasks[0].habit.title} " +
                            "by ${ongoingTasks[0].endTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
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
        val screenModifier = Modifier.fillMaxSize().padding(16.dp)
        when (screenNum) {
            0 -> MiniTasksScreen(userDataViewModel, screenModifier)
            1 -> MiniStatsScreen(userDataViewModel, screenModifier)
            2 -> MiniAchievementsScreen(userDataViewModel, screenModifier)
        }
    }
}

@Composable
fun MiniTaskList(title: String, tasks: List<Task>, userDataViewModel: UserDataViewModel, navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$title:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { navController.navigate("habits") }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "See More")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(8.dp),
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
