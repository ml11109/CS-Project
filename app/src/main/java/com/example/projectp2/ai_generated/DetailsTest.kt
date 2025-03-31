package com.example.projectp2.ai_generated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.model.*
import com.example.projectp2.model.Habit

// This file is AI generated

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestDetailsScreen(userDataViewModel: UserDataViewModel, navController: NavController, habit: Habit) {
    var habitTitle by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Health") }
    var selectedFrequency by remember { mutableStateOf("Daily") }
    var selectedColor by remember { mutableStateOf(Color(0xFF4CAF50)) } // Default green color
    var timePeriod by remember { mutableStateOf("Morning") }
    var showAdvancedSettings by remember { mutableStateOf(false) }

    val categories = listOf("Health", "Productivity", "Learning", "Mindfulness", "Exercise", "Social", "Other")
    val frequencies = listOf("Daily", "Weekly", "Monthly", "Weekdays", "Weekends", "Custom")
    val timePeriods = listOf("Morning", "Afternoon", "Evening", "Night", "Anytime")

    val colors = listOf(
        Color(0xFF4CAF50), // Green
        Color(0xFF2196F3), // Blue
        Color(0xFFF44336), // Red
        Color(0xFF9C27B0), // Purple
        Color(0xFFFF9800), // Orange
        Color(0xFF795548), // Brown
        Color(0xFF607D8B), // Blue Grey
        Color(0xFF009688)  // Teal
    )

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Habit") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val newHabit = Habit(
                                id = 1,
                                title = habitTitle,
                                description = habitDescription,
                                frequency = selectedFrequency,
                                streak = 0,
                                completion = 0f,
                                daysCompleted = ArrayList()
                            )
                            userDataViewModel.habits.add(newHabit)
                        },
                        enabled = habitTitle.isNotBlank()
                    ) {
                        Text("SAVE")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Color picker
            Text(
                text = "Habit Color",
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    items(colors.size) { index -> val color = colors[index]
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = color }
                                .then(
                                    if (selectedColor == color) {
                                        Modifier
                                            .border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                shape = CircleShape
                                            )
                                    } else Modifier
                                )
                        )
                    }
                }
            )

            // Title field
            OutlinedTextField(
                value = habitTitle,
                onValueChange = { habitTitle = it },
                label = { Text("Habit Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                isError = habitTitle.isBlank(),
                supportingText = {
                    if (habitTitle.isBlank()) Text("Title is required")
                },
                leadingIcon = {
                    Icon(Icons.Default.Create, contentDescription = null)
                }
            )

            // Description field
            OutlinedTextField(
                value = habitDescription,
                onValueChange = { habitDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = {
                    Icon(Icons.Default.Create, contentDescription = null)
                }
            )

            // Category selection
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(categories.size) { index -> val category = categories[index]
                        FilterChip(
                            selected = category == selectedCategory,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            leadingIcon = if (category == selectedCategory) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }
            )

            // Frequency selection
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {},
            ) {
                OutlinedTextField(
                    value = selectedFrequency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Frequency") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    leadingIcon = {
                        Icon(Icons.Default.Create, contentDescription = null)
                    }
                )

                ExposedDropdownMenu(
                    expanded = false,
                    onDismissRequest = {}
                ) {
                    frequencies.forEach { frequency ->
                        DropdownMenuItem(
                            text = { Text(frequency) },
                            onClick = { selectedFrequency = frequency },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            // Time period selection
            Text(
                text = "Time Period",
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(timePeriods.size) { index -> val period = timePeriods[index]
                        val icon = when(period) {
                            "Morning" -> Icons.Default.Star
                            "Afternoon" -> Icons.Default.Star
                            "Evening" -> Icons.Default.Star
                            "Night" -> Icons.Default.Star
                            else -> Icons.Default.Star
                        }

                        FilterChip(
                            selected = period == timePeriod,
                            onClick = { timePeriod = period },
                            label = { Text(period) },
                            leadingIcon = {
                                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }
            )

            // Advanced Settings Section
            Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAdvancedSettings = !showAdvancedSettings },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Advanced Settings",
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    if (showAdvancedSettings) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (showAdvancedSettings) "Hide advanced settings" else "Show advanced settings"
                )
            }

            AnimatedVisibility(visible = showAdvancedSettings) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Placeholder for advanced settings
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Advanced settings will be added here",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Space at the bottom for better scrolling experience
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
