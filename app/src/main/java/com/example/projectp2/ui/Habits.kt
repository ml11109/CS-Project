package com.example.projectp2.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.BasicExpandingSearchBar
import com.example.projectp2.composables.DatePickerButton
import com.example.projectp2.composables.DropdownTextBox
import com.example.projectp2.composables.DropdownTextField
import com.example.projectp2.composables.OutlinedExpandingSearchBar
import com.example.projectp2.model.FilterViewModel
import com.example.projectp2.model.Habit
import com.example.projectp2.model.Task
import com.example.projectp2.model.UserDataViewModel

@Composable
fun HabitsScreen(userDataViewModel: UserDataViewModel, navController: NavController) {
    val filterViewModel: FilterViewModel = viewModel()
    val focusManager = LocalFocusManager.current

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
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                }
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
    val modifier = Modifier.height(30.dp)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier.weight(1f).horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DropdownTextBox(
                listOf("All", "Ongoing", "Completed"),
                modifier = modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Status"
            )
            Spacer(modifier.width(8.dp))

            DropdownTextBox(
                userDataViewModel.categories,
                modifier = modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Category"
            )
            Spacer(modifier.width(8.dp))

            DropdownTextBox(
                userDataViewModel.frequencyTypes,
                modifier = modifier.width(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
                textStyle = MaterialTheme.typography.bodySmall,
                initialOption = "Frequency"
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicExpandingSearchBar(
                modifier = modifier.padding(start = 8.dp, end = 4.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                height = 24.dp
            ) {
                filterViewModel.filter.title = it
            }

            DatePickerButton(filterViewModel.calendar, modifier) { _, year, month, day ->
                filterViewModel.calendar.set(year, month, day)
            }
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
