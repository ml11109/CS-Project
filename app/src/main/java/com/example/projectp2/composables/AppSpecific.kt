package com.example.projectp2.composables

import android.app.DatePickerDialog
import android.graphics.Paint
import android.widget.DatePicker
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projectp2.model.UserDataViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

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

@Composable
fun FadeColumn(
    backgroundColor: Color,
    fadeHeight: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fadeHeight)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            backgroundColor,
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fadeHeight)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            backgroundColor
                        )
                    )
                )
        )
    }
}

@Composable
fun FadeRow(
    backgroundColor: Color,
    fadeWidth: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(fadeWidth)
                .align(Alignment.CenterStart)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            backgroundColor,
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(fadeWidth)
                .align(Alignment.CenterEnd)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            backgroundColor
                        )
                    )
                )
        )
    }
}

@Composable
fun PagerSwitcher(
    numPages: Int,
    modifier: Modifier = Modifier,
    getPage: @Composable (screen: Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { numPages + 2 }, initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> { // Scrolled to fake first item
                coroutineScope.launch {
                    pagerState.scrollToPage(numPages)
                }
            }
            numPages + 1 -> { // Scrolled to fake last item
                coroutineScope.launch {
                    pagerState.scrollToPage(1)
                }
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        HorizontalPager(state = pagerState, modifier = modifier) { page -> getPage(page) }

        IconButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
        }

        IconButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous")
        }
    }
}

@Composable
fun WeekSelector(initialDate: LocalDate, modifier: Modifier = Modifier, onDateChange: (LocalDate) -> Unit) {
    var firstDayOfWeek by remember { mutableStateOf(initialDate) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                firstDayOfWeek = firstDayOfWeek.minusWeeks(1)
                onDateChange(firstDayOfWeek)
            }
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous")
        }

        Text(firstDayOfWeek.format(DateTimeFormatter.ofPattern("dd/MM")) + " - " +
                firstDayOfWeek.plusDays(6).format(DateTimeFormatter.ofPattern("dd/MM")))

        IconButton(
            onClick = {
                firstDayOfWeek = firstDayOfWeek.plusWeeks(1)
                onDateChange(firstDayOfWeek)
            }
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
        }
    }
}

data class BarChartItem(val label: String, val value: Float)

@Composable
fun BarChart(
    data: List<BarChartItem>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    labelColor: Color = MaterialTheme.colorScheme.onBackground,
    maxValue: Float = data.maxOfOrNull { it.value } ?: 1f
) {
    val barSpacing = 16.dp
    val labelFontSize = 12.sp
    val labelSpacing = 4.dp

    Canvas(modifier) {
        val barWidth = (size.width - barSpacing.toPx() * (data.size + 1)) / data.size
        val chartHeight = size.height - labelFontSize.toPx() * 2 - labelSpacing.toPx() // leave room for labels

        for (value in 0..maxValue.toInt()) {
            val y = (chartHeight) * (1 - value / maxValue) + labelFontSize.toPx()

            drawContext.canvas.nativeCanvas.drawText(
                value.toString(),
                0f,
                y + labelFontSize.toPx() / 2,
                Paint().apply {
                    color = labelColor.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = labelFontSize.toPx()
                }
            )

            drawLine(
                color = labelColor.copy(alpha = 0.5f),
                start = Offset(labelFontSize.toPx(), y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        data.forEachIndexed { index, item ->
            val barHeight = (item.value / maxValue) * chartHeight

            val left = barSpacing.toPx() + index * (barWidth + barSpacing.toPx())
            val top = size.height - barHeight - labelFontSize.toPx() - labelSpacing.toPx()
            val right = left + barWidth
            val bottom = size.height - labelFontSize.toPx()

            // Draw the bar
            drawRect(
                color = barColor,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight)
            )

            // Draw the label under the bar
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    item.label,
                    left + barWidth / 2,
                    size.height,
                    Paint().apply {
                        color = labelColor.toArgb()
                        textAlign = Paint.Align.CENTER
                        textSize = labelFontSize.toPx()
                    }
                )
            }
        }
    }
}