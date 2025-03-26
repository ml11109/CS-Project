package com.example.projectp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectp2.data.AppViewModel
import com.example.projectp2.ui.HomeScreen
import com.example.projectp2.ui.InfoScreen
import com.example.projectp2.ui.HabitsScreen
import com.example.projectp2.ui.DetailsScreen
import com.example.projectp2.ui.StatsScreen
import com.example.projectp2.ui.SettingsScreen
import com.example.projectp2.ui.OnboardingScreen
import com.example.projectp2.ui.SplashScreen
import com.example.projectp2.ui.theme.ProjectP2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AppNavigation() }
    }
}

@Composable
fun AppNavigation() {
    val appViewModel: AppViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("home") { HomeScreen(appViewModel, navController) }
        composable("habits") { HabitsScreen(appViewModel, navController) }
        composable("details") { DetailsScreen(appViewModel, navController) }
        composable("stats") { StatsScreen(appViewModel, navController) }
        composable("info") { InfoScreen(appViewModel, navController) }
        composable("settings") { SettingsScreen(appViewModel, navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    navController: NavController,
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
                TopAppBar(
                    title = {
                        Text(title, color = MaterialTheme.colorScheme.onPrimary)
                    },

                    navigationIcon = {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, "More", tint = MaterialTheme.colorScheme.onPrimary)
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) { DropdownMenuItems(navController) }
                    },

                    actions = { MenuItems(navController) },

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

@Composable
fun DropdownMenuItems(navController: NavController) {
    DropdownMenuItem(
        text = { Text("Habits") },
        onClick = { navController.navigate("habits") }
    )
    DropdownMenuItem(
        text = { Text("Statistics") },
        onClick = { navController.navigate("stats") }
    )
}

@Composable
fun MenuItems(navController: NavController) {
    Row {
        IconButton(
            onClick = { navController.navigate("info") }
        ) {
            Icon(Icons.Default.Info, "Info", tint = MaterialTheme.colorScheme.onPrimary)
        }

        IconButton(
            onClick = { navController.navigate("settings") }
        ) {
            Icon(Icons.Default.Settings, "Settings", tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}