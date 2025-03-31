package com.example.projectp2.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.BasicExpandingSearchBar
import com.example.projectp2.composables.ExpandingTextField
import com.example.projectp2.composables.OutlinedExpandingSearchBar
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
            BasicExpandingSearchBar {}
            OutlinedExpandingSearchBar {}
            ExpandingTextField(Modifier.fillMaxWidth(), height = 100.dp, isButtonOnRight = false, showHintIfEmpty = true) {}
        }
    }
}

@Composable
fun HabitTextField(field: String, value: String, onValueChange: (String) -> Unit) {
    val required = field in listOf("Title")

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(field) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = required && value.isBlank(),
        supportingText = {
            if (required && value.isBlank()) Text("$field is required")
        },
        leadingIcon = {
            Icon(Icons.Default.Create, contentDescription = null)
        }
    )
}
