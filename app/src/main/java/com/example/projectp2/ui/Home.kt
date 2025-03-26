package com.example.projectp2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.data.AppViewModel
import com.example.projectp2.data.Task

@Composable
fun HomeScreen(appViewModel: AppViewModel, navController: NavController) {
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
        ) {
            InfoBar(appViewModel, Modifier.fillMaxWidth().height(50.dp))
            Spacer(Modifier.height(16.dp))

            MiniStatsScreen(
                appViewModel,
                Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth().weight(0.65f)) {
                TaskList(
                    "Ongoing", appViewModel.getOngoingTasks(), navController,
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(12.dp))
                TaskList(
                    "Upcoming", appViewModel.getUpcomingTasks(), navController,
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
fun AddNewFAB(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("details/0") },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(50)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(24.dp))
    }
}

@Composable
fun InfoBar(appViewModel: AppViewModel, modifier: Modifier = Modifier) {
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
                text = "Welcome back!", // Placeholder text
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
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