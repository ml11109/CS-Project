package com.example.projectp2.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
// Dropdown menu that displays a list of text options

DropdownTextField(listOf("Option 1", "Option 2", "Option 3")) { option = it }

DropdownTextBox(
    listOf("Option 1", "Option 2", "Option 3"),
    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)),
) {
    option = it
}

DropdownSelector(listOf("Option 1", "Option 2", "Option 3"), onValueChange = { option = it }) { selectedText, expanded, contentModifier ->
    // Custom display
}
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    options: List<String>,
    modifier: Modifier = Modifier,
    initialOption: String = "Select an option",
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
                    text = { Text(option) },
                    onClick = {
                        selectedText = option
                        onValueChange(option)
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
    initialOption: String = "Select an option",
    onValueChange: (String) -> Unit
) {
    DropdownSelector(options, modifier, initialOption, onValueChange) { selectedText, expanded, contentModifier ->
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
    initialOption: String = "Select an option",
    onValueChange: (String) -> Unit
) {
    DropdownSelector(options, modifier, initialOption, onValueChange) { selectedText, expanded, contentModifier ->
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

/*
// Text field with button beside that expands it when clicked
// text field contracts to fit text when unfocused
ExpandingTextField { text = it }
 */

@Composable
fun ExpandingTextField(
    modifier: Modifier = Modifier,
    hint: String = "Enter text...",
    width: Dp = 200.dp,
    height: Dp = 54.dp,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isButtonOnRight: Boolean = true,
    showHintIfEmpty: Boolean = false,
    onTextChanged: (String) -> Unit
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val shouldShowHintIfEmpty = showHintIfEmpty && hint.isNotBlank()
    var minWidth by remember { mutableStateOf(
        if (!shouldShowHintIfEmpty) 0.dp else {
            (with(density) {
                textMeasurer.measure(hint, textStyle).size.width.toDp()
            } + 32.dp).coerceAtMost(width)
        }
    ) }

    var expanded by remember { mutableStateOf(false) }
    val currentWidth by animateDpAsState(if (expanded) width else minWidth)
    val alpha by animateFloatAsState(if (expanded) 1f else 0f)
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isButtonOnRight) Arrangement.Start else Arrangement.End
    ) {
        Box {
            OutlinedTextField(
                value = text.ifBlank { hint },
                onValueChange = {},
                singleLine = true,
                textStyle = textStyle,
                modifier = Modifier
                    .width(currentWidth)
                    .height(height)
                    .alpha(1 - alpha),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
            )

            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    onTextChanged(it)
                    minWidth = if (text.isBlank() && !shouldShowHintIfEmpty) 0.dp else {
                        (with(density) {
                            textMeasurer.measure(it.ifBlank { hint }, textStyle).size.width.toDp()
                        } + 32.dp).coerceAtMost(width)
                    }
                },
                placeholder = { Text(hint, style = textStyle) },
                singleLine = true,
                textStyle = textStyle,
                modifier = Modifier
                    .width(currentWidth)
                    .height(height)
                    .alpha(alpha)
                    .focusRequester(focusRequester)
                    .onFocusChanged { expanded = it.isFocused },
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = {
                expanded = !expanded
                if (expanded) {
                    focusRequester.requestFocus()
                } else {
                    focusManager.clearFocus()
                }
            }
        ) {
            Icon(Icons.Default.Edit, null)
        }
    }
}

/*
// Search button that opens a search bar to the side

BasicExpandingSearchBar { text = it } // Smaller, basic text field
OutlinedExpandingSearchBar { text = it } // Larger, outlined text field
ExpandingBarButton { contentModifier ->
    // Custom text field using the given modifier
}
 */

@Composable
fun ExpandingBarButton(
    modifier: Modifier = Modifier,
    width: Dp = 200.dp,
    isButtonOnRight: Boolean = true,
    icon: ImageVector = Icons.Default.Create,
    content: @Composable (contentModifier: Modifier, expanded: Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentWidth by animateDpAsState(if (expanded) width else 0.dp)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val contentModifier = Modifier
        .width(currentWidth)
        .focusRequester(focusRequester)
        .onFocusChanged { if (!it.isFocused) expanded = false }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isButtonOnRight) Arrangement.Start else Arrangement.End
    ) {
        content(contentModifier, expanded)

        IconButton(
            onClick = {
                expanded = !expanded
                if (expanded) {
                    focusRequester.requestFocus()
                } else {
                    focusManager.clearFocus()
                }
            }
        ) {
            Icon(icon, null)
        }
    }
}

@Composable
fun BasicExpandingSearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search...",
    width: Dp = 150.dp,
    height: Dp = 24.dp,
    isButtonOnRight: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    onTextChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    ExpandingBarButton(modifier, width, isButtonOnRight, Icons.Default.Search) { contentModifier, _ ->
        Box(
            modifier = contentModifier,
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)).padding(4.dp),
            ) {
                if (text.isEmpty()) {
                    BasicTextField(
                        value = hint,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        textStyle = textStyle.copy(color = Color.Gray),
                        modifier = Modifier.fillMaxWidth().height(height)
                    )
                }
                BasicTextField(
                    value = text,
                    onValueChange = { text = it; onTextChanged(it) },
                    singleLine = true,
                    textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth().height(height),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
        }
    }
}

@Composable
fun OutlinedExpandingSearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search...",
    width: Dp = 200.dp,
    height: Dp = 54.dp,
    isButtonOnRight: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    onTextChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    ExpandingBarButton(modifier, width, isButtonOnRight, Icons.Default.Search) { contentModifier, _ ->
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; onTextChanged(it) },
            placeholder = { Text(hint) },
            singleLine = true,
            textStyle = textStyle,
            modifier = contentModifier.height(height),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
    }
}
