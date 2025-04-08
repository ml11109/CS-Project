package com.example.projectp2.composables

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.model.UserDataViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownSelector(
    options: List<String>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    initialOption: String = "Select an option",
    userDataViewModel: UserDataViewModel,
    navController: NavController,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(initialOption) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            textStyle = textStyle,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(percent = 50))
                                    .background(userDataViewModel.getCategoryColor(option))
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(option, modifier = Modifier.padding(start = 8.dp), style = textStyle)
                        }
                    },

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

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(
                if (isOn) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .clickable {
                isOn = !isOn
                if (isOn) {
                    DatePickerDialog(
                        context,
                        onDateSelect,
                        date.year,
                        date.month.ordinal,
                        date.dayOfMonth
                    ).show()
                } else {
                    onSwitchOff()
                }
            }
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Date",
            modifier = Modifier.fillMaxSize().padding(4.dp),
            tint = if (isOn) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
        )
    }
}
