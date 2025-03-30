package com.example.projectp2.composables

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

/*
// AlertDialog with TextField

var showDialog by remember { mutableStateOf(false) }

if (showDialog) {
    InputDialog(
        title = "Title",
        onValueSet = { text ->
            // Handle input
        },
        onDismiss = { showDialog = false }
    )
}

// Set showDialog to true to show the dialog
 */

@Composable
fun InputDialog(title: String, onValueSet: (String) -> Unit, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }
    // Add more variables as needed (remember to add the parameters in onValueSet)

    AlertDialog(
        onDismissRequest = onDismiss,

        title = { Text(title) },

        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter text") }
                )
                // Add more input fields as needed
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    onValueSet(text)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

/*
// Button that shows a date picker
val calendar = Calendar.getInstance()

DeadlineDatePicker(calendar) { _, year, month, day ->
    calendar.set(year, month, day)
}
 */

@Composable
fun DatePickerButton(calendar: Calendar, modifier: Modifier = Modifier, onDateSelect: (DatePicker, Int, Int, Int) -> Unit) {
    val context = LocalContext.current

    IconButton(
        modifier = modifier,
        onClick = {
            DatePickerDialog(
                context,
                onDateSelect,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    ) {
        Icon(Icons.Default.DateRange, "Date")
    }
}
