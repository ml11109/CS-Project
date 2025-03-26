package com.example.projectp2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.data.AppViewModel
import com.example.projectp2.data.Task

@Composable
fun HomeScreen(appViewModel: AppViewModel, navController: NavController) {
    AppScaffold (
        "CS Project",
        navController,
        floatingActionButton = { AddNewFAB(navController) }
    ) { nestedScrollConnection ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .nestedScroll(nestedScrollConnection)
        ) {
            InfoBar(
                appViewModel,
                Modifier.fillMaxWidth().height(60.dp)
            )

            Spacer(Modifier.size(12.dp))

            MiniStatsScreen(
                appViewModel,
                Modifier
                    .fillMaxWidth().weight(0.4f)
                    .background(MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.size(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().weight(0.6f)
            ) {
                TaskList(
                    "Ongoing", appViewModel.getOngoingTasks(), navController,
                    Modifier
                        .fillMaxHeight().weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(4.dp))
                )

                Spacer(Modifier.size(8.dp))

                TaskList(
                    "Upcoming", appViewModel.getUpcomingTasks(), navController,
                    Modifier
                        .fillMaxHeight().weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
fun AddNewFAB(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("details/0") }
    ) {
        Icon(Icons.Default.Add, "Add")
    }
}

@Composable
fun InfoBar(appViewModel: AppViewModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Insert text", // TODO
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TaskList(title: String, tasks: ArrayList<Task>, navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$title:",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = { navController.navigate("habits") }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "See More")
            }
        }

        Spacer(Modifier.size(4.dp))

        LazyColumn {
            items(tasks.size) { index ->
                TaskCard(tasks[index])
            }
        }
    }
}
