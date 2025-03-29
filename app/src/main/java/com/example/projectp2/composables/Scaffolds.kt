package com.example.projectp2.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectp2.ui.theme.ProjectP2Theme

/*
// Scaffold with collapsing toolbar
CollapsingToolbarScaffold("Title") { nestedScrollConnection ->
    Column(Modifier.nestedScroll(nestedScrollConnection) {
        // Content
    }
}

// For back button, showBackButton = true and pass in navController, eg:
CollapsingToolbarScaffold("Title", showBackButton = true, navController = navController)

// For dropdown menu, showDropdownMenu = true and pass in dropdownMenuItems, eg:
@Composable
fun DropdownMenuItems() {
    DropdownMenuItem(text = { Text("Item 1") }, onClick = { /* Handle click */ })
    DropdownMenuItem(text = { Text("Item 2") }, onClick = { /* Handle click */ })
}
CollapsingToolbarScaffold("Title", showDropdownMenu = true, dropdownMenuItems = { DropdownMenuItems() })

// Can also set showAppBar to false, pass in floatingActionButton, or pass in other menuItems
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbarScaffold(
    title: String = "",
    showAppBar: Boolean = true,
    showBackButton: Boolean = false,
    navController: NavController? = null,
    showDropdownMenu: Boolean = false,
    menuItems: @Composable () -> Unit = {},
    dropdownMenuItems: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (nestedScrollConnection: NestedScrollConnection) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var menuExpanded by remember { mutableStateOf(false) }

    ProjectP2Theme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),

            floatingActionButton = floatingActionButton,

            topBar = {
                if (!showAppBar) return@Scaffold

                TopAppBar(
                    title = {
                        val modifier = if (showBackButton) Modifier else Modifier.padding(start = 8.dp)
                        Text(title, color = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
                    },

                    navigationIcon = {
                        if (!showBackButton) return@TopAppBar

                        IconButton(onClick = {
                            navController?.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },

                    actions = {
                        if (showDropdownMenu) {
                            Row {
                                menuItems()

                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(Icons.Default.MoreVert, "More", tint = MaterialTheme.colorScheme.onPrimary)
                                }

                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {
                                    dropdownMenuItems()
                                }
                            }
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),

                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                content(scrollBehavior.nestedScrollConnection)
            }
        }
    }
}

/*
// Scaffold with toolbar
ToolbarScaffold("Title") {
    // Content
}

// For back button, showBackButton = true and pass in navController, eg:
ToolbarScaffold("Title", showBackButton = true, navController = navController)

// For dropdown menu, showDropdownMenu = true and pass in dropdownMenuItems, eg:
@Composable
fun DropdownMenuItems() {
    DropdownMenuItem(text = { Text("Item 1") }, onClick = { /* Handle click */ })
    DropdownMenuItem(text = { Text("Item 2") }, onClick = { /* Handle click */ })
}
ToolbarScaffold("Title", showDropdownMenu = true, dropdownMenuItems = { DropdownMenuItems() })

// Can also set showAppBar to false, pass in floatingActionButton, or pass in other menuItems
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarScaffold(
    title: String = "",
    showAppBar: Boolean = true,
    showBackButton: Boolean = false,
    navController: NavController? = null,
    showDropdownMenu: Boolean = false,
    menuItems: @Composable () -> Unit = {},
    dropdownMenuItems: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    ProjectP2Theme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),

            floatingActionButton = floatingActionButton,

            topBar = {
                if (!showAppBar) return@Scaffold

                TopAppBar(
                    title = {
                        val modifier = if (showBackButton) Modifier else Modifier.padding(start = 8.dp)
                        Text(title, color = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
                    },

                    navigationIcon = {
                        if (!showBackButton) return@TopAppBar

                        IconButton(onClick = {
                            navController?.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },

                    actions = {
                        if (showDropdownMenu) {
                            Row {
                                menuItems()

                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(Icons.Default.MoreVert, "More", tint = MaterialTheme.colorScheme.onPrimary)
                                }

                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {
                                    dropdownMenuItems()
                                }
                            }
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                )
            }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                content()
            }
        }
    }
}
