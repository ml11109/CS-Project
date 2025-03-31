package com.example.projectp2.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.BasicExpandingSearchBar
import com.example.projectp2.composables.DropdownTextField
import com.example.projectp2.composables.ExpandingTextField
import com.example.projectp2.composables.OutlinedExpandingSearchBar
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
            TitleTextField(habit)
            Spacer(Modifier.height(16.dp))

            DescriptionTextField(habit)
            Spacer(Modifier.height(16.dp))

            CategoryTextField(userDataViewModel, habit)
        }
    }
}

@Composable
private fun TitleTextField(habit: Habit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Title: ")
        Spacer(Modifier.width(16.dp))
        BoxWithConstraints {
            val boxWithConstraintsScope = this
            ExpandingTextField(
                hint = "Enter title...",
                width = boxWithConstraintsScope.maxWidth - 44.dp,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            ) {
                habit.title = it
            }
        }
    }
}

@Composable
private fun DescriptionTextField(habit: Habit) {
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
private fun CategoryTextField(userDataViewModel: UserDataViewModel, habit: Habit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Category: ")
        Spacer(Modifier.width(16.dp))
        DropdownTextField(
            options = userDataViewModel.categories,
            initialOption = if (habit.category == Category.NONE) "Select a category" else habit.category,
        ) {
            habit.category = it
        }
    }
}
