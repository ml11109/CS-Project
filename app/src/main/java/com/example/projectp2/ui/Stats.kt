package com.example.projectp2.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.R
import com.example.projectp2.composables.BarChart
import com.example.projectp2.composables.BarChartItem
import com.example.projectp2.composables.FadeColumn
import com.example.projectp2.composables.OptionsRow
import com.example.projectp2.composables.TabScreen
import com.example.projectp2.composables.WeekSelector
import com.example.projectp2.model.AchievementCard
import com.example.projectp2.model.Task
import com.example.projectp2.model.TaskStatus
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Week
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate

@Composable
fun StatisticsScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    AppScaffold(
        title = "Statistics",
        navController = navController,
        drawerState = drawerState,
        scope = scope,
        floatingActionButton = { AddNewFAB(navController) }
    ) { nestedScrollConnection ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(rememberScrollState())
        ) {
            TabScreen(
                numTabs = 3,
                tabTitles = listOf("Graph", "Statistics", "Achievements"),
                tabIcons = listOf<@Composable () -> Unit>(
                    { Icon(painterResource(R.drawable.chart), "chart", Modifier.size(20.dp)) },
                    { Icon(Icons.AutoMirrored.Filled.List, "stats", Modifier.size(24.dp)) },
                    { Icon(Icons.Default.Star, "achievements", Modifier.size(24.dp)) },
                ),
                modifier = Modifier.fillMaxWidth().weight(1f),
                tabRowModifier = Modifier.fillMaxWidth().height(70.dp)
            ) {
                val screenModifier = Modifier.fillMaxSize().padding(16.dp)
                when (it) {
                    0 -> GraphScreen(userDataViewModel, screenModifier)
                    1 -> StatsScreen(userDataViewModel, screenModifier)
                    2 -> AchievementsScreen(userDataViewModel, Modifier.fillMaxSize().padding(horizontal = 32.dp))
                }
            }
        }
    }
}

@Composable
fun GraphScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    var firstDayOfWeek by remember { mutableStateOf(LocalDate.now().minusDays(LocalDate.now().dayOfWeek.ordinal.toLong())) }
    var status by remember { mutableStateOf(TaskStatus.ALL) }
    val chartData = remember { mutableStateListOf<BarChartItem>().apply { repeat(7) { add(BarChartItem("", 0f)) } } }

    for (dayOfWeek in 0 until 7) {
        val date = firstDayOfWeek.plusDays(dayOfWeek.toLong())
        val label = Week.getDayName(dayOfWeek + 1).first().toString()
        val tasks: List<Task> = when (status) {
            TaskStatus.ALL -> userDataViewModel.getTasks { it.date == date }
            TaskStatus.ONGOING -> userDataViewModel.getTasks { it.date == date && it.isOngoing() }
            TaskStatus.UPCOMING -> userDataViewModel.getTasks { it.date == date && it.isUpcoming() }
            TaskStatus.COMPLETED -> userDataViewModel.getTasks { it.date == date && it.isCompleted() }
            TaskStatus.FAILED -> userDataViewModel.getTasks { it.date == date && it.isFailed() }
            else -> listOf()
        }
        val value = tasks.size.toFloat()
        chartData[dayOfWeek] = BarChartItem(label, value)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Tasks per day",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            OptionsRow(
                options = arrayListOf(TaskStatus.ALL) + userDataViewModel.taskStatusTypes,
                initialOption = TaskStatus.ONGOING,
                backgroundColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().height(30.dp)
            ) {
                status = it
            }

            if (chartData.maxOfOrNull { it.value } !in listOf(null, 0f)) {
                BarChart(
                    data = chartData,
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.alpha(0.5f)
                    )
                }
            }
        }

        WeekSelector(firstDayOfWeek) { firstDayOfWeek = it }
    }
}

@Composable
fun StatsScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier, mini: Boolean = false) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 32.dp, vertical = if (mini) 0.dp else 16.dp)
        ) {
            FadeColumn(
                backgroundColor = MaterialTheme.colorScheme.surface,
                fadeHeight = if (mini) 12.dp else 16.dp,
                modifier = Modifier
            ) {
                val numColumns = if (mini || LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2

                LazyVerticalGrid(
                    columns = GridCells.Fixed(numColumns),
                    verticalArrangement = Arrangement.spacedBy(if (mini) 12.dp else 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(64.dp)
                ) {
                    items(numColumns) { Spacer(Modifier) }

                    items(userDataViewModel.statistics.size) { index ->
                        val statistic = userDataViewModel.statistics[index]

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = statistic.description,
                                style = MaterialTheme.typography.titleMedium
                            )

                            HorizontalDivider(
                                Modifier.weight(1f).padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )

                            Text(
                                text = statistic.value.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    items(numColumns) { Spacer(Modifier) }
                }
            }
        }
    }
}

@Composable
fun AchievementsScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier, mini: Boolean = false) {
    val achievements = userDataViewModel.achievements

    FadeColumn(
        backgroundColor = MaterialTheme.colorScheme.surface,
        fadeHeight = if (mini) 8.dp else 0.dp,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(Modifier.height(if (mini) 0.dp else 8.dp)) }

            items(achievements.size) { index ->
                AchievementCard(
                    achievements[index],
                    Modifier.fillMaxWidth(),
                )
            }
            item { Spacer(Modifier) }
        }
    }
}
