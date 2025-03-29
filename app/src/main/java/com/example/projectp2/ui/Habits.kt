package com.example.projectp2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.example.projectp2.model.Habit
import com.example.projectp2.model.Task
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.utils.DropdownTextBox

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
            FilterOptions(userDataViewModel, filterViewModel)
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
fun FilterOptions(userDataViewModel: UserDataViewModel, filterViewModel: FilterViewModel) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)
    ) {
        DropdownTextBox(
            listOf("All", "Ongoing", "Completed"),
            modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
            textStyle = MaterialTheme.typography.bodySmall,
            initialOption = "Status"
        )
        Spacer(Modifier.width(4.dp))

        DropdownTextBox(
            userDataViewModel.categories,
            modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
            textStyle = MaterialTheme.typography.bodySmall,
            initialOption = "Category"
        )
        Spacer(Modifier.width(4.dp))

        DropdownTextBox(
            userDataViewModel.frequencyTypes,
            modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
            textStyle = MaterialTheme.typography.bodySmall,
            initialOption = "Frequency"
        )
        Spacer(Modifier.width(4.dp))

        // Add date selector and search bar
    }
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
