package com.example.projectp2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Task

@Composable
fun HomeScreen(userDataViewModel: UserDataViewModel, navController: NavController) {
    AppScaffold(
        title = "CS Project",
        navController = navController,
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
                Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth().weight(0.6f)) {
                TaskList(
                    "Ongoing", userDataViewModel.getOngoingTasks(), navController,
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(12.dp))
                TaskList(
                    "Upcoming", userDataViewModel.getUpcomingTasks(), navController,
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
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
    var screenNum by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
    ) {
        val screenModifier = Modifier.fillMaxSize().padding(16.dp)
        when (screenNum) {
            0 -> MiniHabitsScreen(userDataViewModel, screenModifier)
            1 -> MiniStatsScreen(userDataViewModel, screenModifier)
            2 -> MiniAchievementsScreen(userDataViewModel, screenModifier)
        }

        IconButton(
            onClick = { screenNum = (screenNum + 1) % 3 },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
        }

        IconButton(
            onClick = { screenNum = (screenNum + 2) % 3 },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
        }
    }
}

@Composable
fun TaskList(title: String, tasks: List<Task>, navController: NavController, modifier: Modifier = Modifier) {
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
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
            items(tasks.size) { index ->
                TaskCard(tasks[index])
            }
        }
    }
}
