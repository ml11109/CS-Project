package com.example.projectp2.ui

import android.text.TextUtils.replace
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectp2.composables.DropdownTextBox
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.DatePickerButton
import com.example.projectp2.model.FilterViewModel
import com.example.projectp2.model.Habit
import com.example.projectp2.model.Task
import com.example.projectp2.model.UserDataViewModel

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOptions(userDataViewModel: UserDataViewModel, filterViewModel: FilterViewModel) {
    val scrollState = rememberScrollState()
    val textFieldState = rememberTextFieldState()
    var expanded = false

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DropdownTextBox(
                listOf("All", "Ongoing", "Completed"),
                modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Status"
            )
            Spacer(Modifier.width(8.dp))

            DropdownTextBox(
                userDataViewModel.categories,
                modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Category"
            )
            Spacer(Modifier.width(8.dp))

            DropdownTextBox(
                userDataViewModel.frequencyTypes,
                modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Frequency"
            )
        }

        DatePickerButton(filterViewModel.calendar) { _, year, month, day ->
            filterViewModel.calendar.set(year, month, day)
        }
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
