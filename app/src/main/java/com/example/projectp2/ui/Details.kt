package com.example.projectp2.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.model.Habit
import com.example.projectp2.model.UserDataViewModel

@Composable
fun DetailsScreen(userDataViewModel: UserDataViewModel, navController: NavController, habit: Habit) {
    AppScaffold(
        title = if (habit.id == 0) "New Habit" else "Edit Habit",
        navController = navController
    ) { }
}