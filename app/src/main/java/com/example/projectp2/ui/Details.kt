package com.example.projectp2.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.DropdownTextField
import com.example.projectp2.composables.ExpandingTextField
import com.example.projectp2.model.Category
import com.example.projectp2.model.Habit
import com.example.projectp2.model.UserDataViewModel

@Composable
fun DetailsScreen(userDataViewModel: UserDataViewModel, navController: NavController, habitId: Int) {
    val focusManager = LocalFocusManager.current
    val habit = userDataViewModel.getHabitFromId(habitId)

    AppScaffold(
        title = if (habitId == -1) "New Habit" else "Edit Habit",
        navController = navController
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
            TitleTextField(habit, Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))

            DescriptionTextField(habit, Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Category", Modifier.padding(start = 16.dp, bottom = 8.dp))
                Spacer(Modifier.width(16.dp))
                CategoryTextField(userDataViewModel, habit, Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun TitleTextField(habit: Habit, modifier: Modifier = Modifier) {
    BoxWithConstraints {
        val boxWithConstraintsScope = this
        ExpandingTextField(
            modifier = modifier,
            hint = "Enter title...",
            width = boxWithConstraintsScope.maxWidth - 44.dp,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            showHintIfEmpty = true
        ) {
            habit.title = it
        }
    }
}

@Composable
private fun DescriptionTextField(habit: Habit, modifier: Modifier = Modifier) {
    var description by remember { mutableStateOf(habit.description) }

    OutlinedTextField(
        value = description,
        onValueChange = { description = it; habit.description = it },
        label = { Text("Description") },
        minLines = 2,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}

@Composable
private fun CategoryTextField(userDataViewModel: UserDataViewModel, habit: Habit, modifier: Modifier = Modifier) {
    DropdownTextField(
        options = userDataViewModel.categories,
        textStyle = MaterialTheme.typography.bodyLarge,
        initialOption = if (habit.category == Category.NONE) "Select a category" else habit.category,
    ) {
        habit.category = it
    }
}
