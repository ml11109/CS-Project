package com.example.projectp2.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.model.FilterViewModel
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.Habit
import com.example.projectp2.model.Task

@Composable
fun HabitsScreen(userDataViewModel: UserDataViewModel, navController: NavController) {
    val filterViewModel: FilterViewModel = viewModel()

    AppScaffold(
        title = "Habits",
        navController = navController,
        floatingActionButton = { AddNewFAB(navController) }
    ) { nestedScrollConnection ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .nestedScroll(nestedScrollConnection)
        ) {
            FilterOptions(filterViewModel)
            HabitList(filterViewModel.filterHabits(userDataViewModel.habits))
        }
    }
}

@Composable
fun MiniHabitsScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Habits")
    }
}

@Composable
fun FilterOptions(filterViewModel: FilterViewModel) {

}

@Composable
fun HabitList(habits: MutableList<Habit>) {

}

@Composable
fun TaskCard(task: Task) {

}

@Composable
fun HabitCard(habit: Habit) {

}
