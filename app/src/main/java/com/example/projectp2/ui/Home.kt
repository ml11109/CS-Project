package com.example.projectp2.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.R
import com.example.projectp2.composables.ScreenSwitcher
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Task
import kotlinx.coroutines.CoroutineScope
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    val boxModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))

    AppScaffold(
        title = stringResource(R.string.app_name),
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
        ) {
            InfoBar(userDataViewModel, Modifier.fillMaxWidth().height(50.dp))
            Spacer(Modifier.height(16.dp))

            MiniScreen(
                userDataViewModel,
                boxModifier.fillMaxWidth().height(300.dp)
            )
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                TaskColumn(
                    "Ongoing", userDataViewModel.getOngoingTasks(),
                    userDataViewModel, navController,
                    boxModifier.fillMaxHeight().weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                TaskColumn(
                    "Upcoming", userDataViewModel.getUpcomingTasks(10),
                    userDataViewModel, navController,
                    boxModifier.fillMaxHeight().weight(1f)
                )
            }
        }
    }
}

@Composable
fun InfoBar(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Welcome back!", // TODO: Add info text
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MiniScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    ScreenSwitcher(3, 0, modifier) { screenNum ->
        val screenModifier = Modifier.fillMaxSize().padding(16.dp)
        when (screenNum) {
            0 -> MiniHabitsScreen(userDataViewModel, screenModifier)
            1 -> MiniStatsScreen(userDataViewModel, screenModifier)
            2 -> MiniAchievementsScreen(userDataViewModel, screenModifier)
        }
    }
}

@Composable
fun TaskColumn(title: String, tasks: List<Task>, userDataViewModel: UserDataViewModel, navController: NavController, modifier: Modifier = Modifier) {
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
                onClick = { navController.navigate("habits/${title.lowercase()}") }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "See More")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
            items(tasks.size) { index ->
                TaskCard(
                    userDataViewModel,
                    tasks[index],
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TaskCard(userDataViewModel: UserDataViewModel, task: Task, modifier: Modifier = Modifier) {
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
            // Task details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.habit.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    // Color indicator
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(percent = 50))
                            .background(userDataViewModel.getCategoryColor(task.habit.category))
                    )
                }

                Text(
                    text = task.habit.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.startTime.format(DateTimeFormatter.ofPattern("hh:mm a")) + " - "
                                + task.endTime.format(DateTimeFormatter.ofPattern("hh:mm a")) + ", "
                                + task.date.format(DateTimeFormatter.ofPattern("dd/MM")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
