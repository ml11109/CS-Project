package com.example.projectp2.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/*
// Dropdown menu that displays a list of text options

DropdownTextField(listOf("Option 1", "Option 2", "Option 3"))

DropdownTextBox(
    listOf("Option 1", "Option 2", "Option 3"),
    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
)

DropdownSelector(listOf("Option 1", "Option 2", "Option 3")) { selectedText, expanded, contentModifier ->
    // Custom display
}
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    options: List<String>,
    modifier: Modifier = Modifier,
    initialOption: String = "Select an option",
    content: @Composable (selectedText: String, expanded: Boolean, modifier: Modifier) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(initialOption) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        content(selectedText, expanded, Modifier.menuAnchor())

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedText = option
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTextField(
    options: List<String>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    initialOption: String = "Select an option"
) {
    DropdownSelector(options, modifier, initialOption) { selectedText, expanded, contentModifier ->
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            textStyle = textStyle,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = contentModifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTextBox(
    options: List<String>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    initialOption: String = "Select an option"
) {
    DropdownSelector(options, modifier, initialOption) { selectedText, expanded, contentModifier ->
        Row(
            modifier = contentModifier.fillMaxSize().padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(4.dp))
            Text(
                text = selectedText,
                style = textStyle
            )
            Spacer(Modifier.weight(1f))
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded
            )
        }
    }
}
