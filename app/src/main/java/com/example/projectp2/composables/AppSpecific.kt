package com.example.projectp2.composables

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownSelector(
    options: List<String>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    initialOption: String = "Select an option",
    navController: NavController,
    onValueChange: (String) -> Unit,
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
                    text = { Text(option, modifier = Modifier.padding(start = 8.dp), style = textStyle) },
                    onClick = {
                        selectedText = option
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
            DropdownMenuItem(
                text = { Text("Manage categories", modifier = Modifier.fillMaxWidth(),
                    style = textStyle, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center) },
                onClick = {
                    navController.navigate("settings/categories")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownTextField(
    options: List<String>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    initialOption: String = "Select an option",
    navController: NavController,
    onValueChange: (String) -> Unit
) {
    CustomDropdownSelector(options, modifier, textStyle, initialOption, navController, onValueChange) { selectedText, expanded, contentModifier ->
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

@Composable
fun DatePickerSwitch(
    date: LocalDate,
    modifier: Modifier = Modifier,
    on: Boolean = false,
    onDateSelect: (DatePicker, Int, Int, Int) -> Unit,
    onSwitchOff: () -> Unit
) {
    val context = LocalContext.current
    var isOn by remember { mutableStateOf(on) }

    IconButton(
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = if (isOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        ),
        onClick = {
            isOn = !isOn
            if (isOn) {
                DatePickerDialog(
                    context,
                    onDateSelect,
                    date.year,
                    date.monthValue,
                    date.dayOfMonth
                ).show()
            } else {
                onSwitchOff()
            }
        }
    ) {
        Icon(Icons.Default.DateRange, "Date")
    }
}
