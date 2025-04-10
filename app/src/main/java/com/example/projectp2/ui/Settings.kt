package com.example.projectp2.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.AddNewFAB
import com.example.projectp2.AppScaffold
import com.example.projectp2.composables.ColorPickerDialog
import com.example.projectp2.composables.ExpandingTextField
import com.example.projectp2.composables.TabScreen
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.model.colors
import com.example.projectp2.ui.theme.AppTheme
import com.example.projectp2.ui.theme.ProjectP2Theme
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingsScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope, setting: String) {
    AppScaffold(
        title = "Settings",
        navController = navController,
        drawerState = drawerState,
        scope = scope,
        floatingActionButton = { AddNewFAB(navController) }
    ) { nestedScrollConnection ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(rememberScrollState())
        ) {
            TabScreen(
                numTabs = 2,
                initialTabIndex = if (setting == "categories") 1 else 0,
                tabTitles = listOf("Theme", "Categories"),
                modifier = Modifier.fillMaxWidth().weight(1f),
                tabRowModifier = Modifier.fillMaxWidth().height(50.dp),
                textStyle = MaterialTheme.typography.titleMedium
            ) {
                val screenModifier = Modifier.fillMaxSize().padding(16.dp)
                when (it) {
                    0 -> ThemeSettingsScreen(userDataViewModel, navController, screenModifier)
                    1 -> CategoriesSettingsScreen(userDataViewModel, navController, screenModifier)
                }
            }
        }
    }
}

@Composable
fun ThemeSettingsScreen(userDataViewModel: UserDataViewModel, navController: NavController, modifier: Modifier = Modifier) {
    var themeIndex by remember { mutableIntStateOf(userDataViewModel.theme.ordinal) }
    val themes = AppTheme.entries
    val colors = if (MaterialTheme.colorScheme.background.luminance() < 0.5) {
        listOf(Color.LightGray, Color.White, Color.Gray, Color(0xFF2E7D32), Color(0xFF90CAF9),
            Color(0xFFE91E63), Color(0xFF0288D1), Color(0xFFFFA726))
    } else {
        listOf(Color.DarkGray, Color.Gray, Color.Black, Color(0xFF2E7D32), Color(0xFF90CAF9),
            Color(0xFFE91E63), Color(0xFF0288D1), Color(0xFFFFA726))
    }
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val numColumns = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
        LazyVerticalGrid(
            columns = GridCells.Fixed(numColumns),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text("Theme:", Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp), style = MaterialTheme.typography.headlineSmall)
            }

            items(themes.size) { index ->
                Row(
                    modifier.clickable { themeIndex = index },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = themeIndex == index,
                        modifier = Modifier.size(24.dp),
                        onClick = { themeIndex = index }
                    )
                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = themes[index].name,
                        style = MaterialTheme.typography.titleLarge,
                        color = colors[index]
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HorizontalDivider(Modifier.fillMaxWidth().padding(top = 16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))

                        Button(
                            onClick = {
                                navController.popBackStack()
                            },
                            modifier = Modifier.width(110.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text("Cancel", style = MaterialTheme.typography.titleMedium)
                        }
                        Spacer(Modifier.width(12.dp))

                        Button(
                            onClick = {
                                navController.popBackStack()
                                userDataViewModel.theme = themes[themeIndex]
                                userDataViewModel.saveTheme(context)
                            },
                            modifier = Modifier.width(110.dp),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text("Save", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriesSettingsScreen(userDataViewModel: UserDataViewModel, navController: NavController, modifier: Modifier) {
    val categories = remember { mutableStateListOf<String>().apply { addAll(userDataViewModel.categories) } }
    val categoryColors = remember { mutableStateListOf<Color>().apply { addAll(userDataViewModel.categoryColors) } }
    var showColorDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val numColumns = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
        LazyVerticalGrid(
            columns = GridCells.Fixed(numColumns),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Categories", Modifier.padding(start = 8.dp), style = MaterialTheme.typography.titleLarge)
                        IconButton(
                            onClick = {
                                categories.add("New Category")
                                categoryColors.add(Color.White)
                            }
                        ) {
                            Icon(Icons.Default.Add, "Add")
                        }
                    }
                    HorizontalDivider(Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                }
            }

            items(categories.size) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(categoryColors[index])
                            .clickable {
                                selectedIndex = index
                                showColorDialog = true
                            }
                    )
                    Spacer(Modifier.width(12.dp))

                    BoxWithConstraints(
                        modifier = Modifier.weight(1f)
                    ) {
                        ExpandingTextField(
                            text = categories[index],
                            hint = "Enter category name...",
                            width = maxWidth - 36.dp,
                            textStyle = MaterialTheme.typography.titleLarge
                        ) {
                            categories[index] = it
                        }
                    }
                    Spacer(Modifier.width(12.dp))

                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            categories.remove(categories[index])
                            categoryColors.remove(categoryColors[index])
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HorizontalDivider(Modifier.fillMaxWidth().padding(horizontal = 16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))

                        Button(
                            onClick = {
                                navController.popBackStack()
                            },
                            modifier = Modifier.width(110.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text("Cancel", style = MaterialTheme.typography.titleMedium)
                        }
                        Spacer(Modifier.width(12.dp))

                        Button(
                            onClick = {
                                navController.popBackStack()
                                userDataViewModel.categories.clear()
                                userDataViewModel.categoryColors.clear()
                                userDataViewModel.categories.addAll(categories)
                                userDataViewModel.categoryColors.addAll(categoryColors)
                                userDataViewModel.saveCategories(context)
                            },
                            enabled = categories.size > 0,
                            modifier = Modifier.width(110.dp),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text("Save", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }

        if (categories.isNotEmpty()) {
            ColorPickerDialog(
                show = showColorDialog,
                title = "${categories[selectedIndex]} category color:",
                colors = colors,
                selectedColor = categoryColors[selectedIndex],
                onColorSelected = { categoryColors[selectedIndex] = it },
                onDismiss = { showColorDialog = false }
            )
        }
    }
}
