package com.example.projectp2.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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
