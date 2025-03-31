package com.example.projectp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.navigation.get
import com.example.projectp2.ai_generated.HabitTrackerScreen
import com.example.projectp2.model.Habit
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.ui.AchievementsScreen
import com.example.projectp2.ui.HomeScreen
import com.example.projectp2.ui.InfoScreen
import com.example.projectp2.ui.HabitsScreen
import com.example.projectp2.ui.DetailsScreen
import com.example.projectp2.ui.StatsScreen
import com.example.projectp2.ui.SettingsScreen
import com.example.projectp2.ui.OnboardingScreen
import com.example.projectp2.ui.SplashScreen
import com.example.projectp2.ai_generated.TestDetailsScreen
import com.example.projectp2.ui.theme.ProjectP2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { ProjectP2Theme { AppNavigation() } }
    }
}

@Composable
fun AppNavigation() {
    val userDataViewModel: UserDataViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("home") { HomeScreen(userDataViewModel, navController) }
        composable("habits") { HabitsScreen(userDataViewModel, navController) }

        composable("details/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")
            DetailsScreen(userDataViewModel, navController, userDataViewModel.getHabitFromId(habitId!!.toInt()))
        }

        composable("stats") { StatsScreen(userDataViewModel, navController) }
        composable("achievements") { AchievementsScreen(userDataViewModel, navController) }
        composable("info") { InfoScreen(userDataViewModel, navController) }
        composable("settings") { SettingsScreen(userDataViewModel, navController) }

        composable("test") { HabitTrackerScreen() }
        composable("details test") { TestDetailsScreen(userDataViewModel, navController, Habit()) }
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
                        Icon(Icons.Default.Menu, "More", tint = MaterialTheme.colorScheme.onPrimary)
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItems(navController) { menuExpanded = false }
                    }
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

@Composable
fun DropdownMenuItems(navController: NavController, onDismissRequest: () -> Unit) {
    val screenRoutes = listOf("home", "habits", "stats", "achievements", "test", "details test")
    val screenTitles = listOf("Home", "Habits", "Statistics", "Achievements", "test", "details test")

    for (screen in screenRoutes.indices) {
        if (navController.currentDestination != navController.graph[screenRoutes[screen]]) {
            DropdownMenuItem(
                text = { Text(screenTitles[screen]) },
                onClick = {
                    onDismissRequest()
                    navController.navigate(screenRoutes[screen])
                }
            )
        }
    }
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

@Composable
fun AddNewFAB(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("details/0") },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(50)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(24.dp))
    }
}
