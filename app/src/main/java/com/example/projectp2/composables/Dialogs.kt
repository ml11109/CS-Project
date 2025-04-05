package com.example.projectp2.composables

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate
import java.time.LocalTime

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
val date = LocalDate.now()

DeadlineDatePicker(calendar) { _, year, month, dayOfMonth ->
    date = LocalDate.of(year, month + 1, dayOfMonth)
}
 */

@Composable
fun DatePickerButton(date: LocalDate, modifier: Modifier = Modifier, onDateSelect: (DatePicker, Int, Int, Int) -> Unit) {
    val context = LocalContext.current

    IconButton(
        modifier = modifier,
        onClick = {
            DatePickerDialog(
                context,
                onDateSelect,
                date.year,
                date.month.ordinal,
                date.dayOfMonth
            ).show()
        }
    ) {
        Icon(Icons.Default.DateRange, "Date")
    }
}

@Composable
fun TimePickerButton(time: LocalTime, modifier: Modifier = Modifier, onTimeSelect: (TimePicker, Int, Int) -> Unit) {
    val context = LocalContext.current

    IconButton(
        modifier = modifier,
        onClick = {
            TimePickerDialog(
                context,
                onTimeSelect,
                time.hour,
                time.minute,
                false
            ).show()
        }
    ) {
        Icon(Icons.Default.Edit, "Time")
    }
}

@Composable
fun BasicAlertDialog(
    showAlertDialog: Boolean,
    icon: ImageVector,
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    if (showAlertDialog) {
        AlertDialog(
            icon = {
                Icon(icon, contentDescription = null)
            },
            title = {
                Text(title, textAlign = TextAlign.Center)
            },
            text = {
                Text(text, textAlign = TextAlign.Center)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}
