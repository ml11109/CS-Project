package com.example.projectp2.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
// Search button that opens a search bar to the side

BasicExpandingSearchBar { text = it } // Smaller, basic text field
OutlinedExpandingSearchBar { text = it } // Larger, outlined text field
ExpandingSearchBarButton { contentModifier ->
    // Custom text field using the given modifier
}
 */

@Composable
fun ExpandingSearchBarButton(
    modifier: Modifier = Modifier,
    width: Dp = 200.dp,
    isButtonOnRight: Boolean = true,
    content: @Composable (contentModifier: Modifier) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val currentWidth by animateDpAsState(if (isExpanded) width else 0.dp)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val contentModifier = Modifier
        .width(currentWidth)
        .focusRequester(focusRequester)
        .onFocusChanged { if (!it.isFocused) isExpanded = false }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isButtonOnRight) Arrangement.Start else Arrangement.End
    ) {
        content(contentModifier)

        IconButton(
            onClick = {
                isExpanded = !isExpanded
                if (!isExpanded) {
                    focusRequester.requestFocus()
                } else {
                    focusManager.clearFocus()
                }
            }
        ) {
            Icon(Icons.Default.Search, "Search")
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

    ExpandingSearchBarButton(modifier, width, isButtonOnRight) { contentModifier ->
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
                        enabled = false,
                        textStyle = textStyle.copy(color = Color.Gray),
                        modifier = Modifier.fillMaxWidth().height(height)
                    )
                }
                BasicTextField(
                    value = text,
                    onValueChange = { text = it; onTextChanged(it) },
                    singleLine = true,
                    textStyle = textStyle,
                    modifier = Modifier.fillMaxWidth().height(height)
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

    ExpandingSearchBarButton(modifier, width, isButtonOnRight) { contentModifier ->
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; onTextChanged(it) },
            placeholder = { Text(hint) },
            singleLine = true,
            textStyle = textStyle,
            modifier = contentModifier.height(height)
        )
    }
}
