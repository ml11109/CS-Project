package com.example.projectp2.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projectp2.R
import com.example.projectp2.model.Habit
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.ui.theme.ProjectP2Theme
import com.example.projectp2.util.createNotificationChannel
import com.example.projectp2.util.hasSeenOnboarding
import com.example.projectp2.util.setOnboardingSeen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AppEntry() }
    }
}

@Composable
fun AppEntry() {
    val userDataViewModel: UserDataViewModel = viewModel()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    userDataViewModel.loadData(context)
    userDataViewModel.updateStreak(context)
    createNotificationChannel(context)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(userDataViewModel, navController, drawerState, scope)
        }
    ) {
        AppNavigation(userDataViewModel, navController, drawerState, scope)
    }
}

@Composable
fun AppNavigation(userDataViewModel: UserDataViewModel, navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val context = LocalContext.current

    NavHost(navController, startDestination = "splash") {
        composable(
            "splash"
        ) {
            ProjectP2Theme(userDataViewModel.theme) {
                SplashScreen(
                    onFinished = {
                        val next = if (hasSeenOnboarding(context)) "home" else "onboarding"
                        navController.navigate(next) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(
            "onboarding",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(700)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(700)) }
        ) {
            ProjectP2Theme(userDataViewModel.theme) {
                OnboardingScreen(
                    onComplete = {
                        setOnboardingSeen(context)
                        navController.navigate("home") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }
        }

        composable("home") { ProjectP2Theme(userDataViewModel.theme) { HomeScreen(userDataViewModel, navController, drawerState, scope) } }
        composable("habits") { ProjectP2Theme(userDataViewModel.theme) { HabitsScreen(userDataViewModel, navController, drawerState, scope) } }
        composable("details/{habitType}/{habitIndex}") { backStackEntry ->
            // 0 habitType means new habit, 1 means edit habit, 2 means copied habit, 3 means habit from template
            val habitType = backStackEntry.arguments?.getString("habitType")!!.toInt()
            val habitIndex = backStackEntry.arguments?.getString("habitIndex")!!.toInt()

            val habit = when (habitType) {
                0 -> Habit()
                1 -> userDataViewModel.habits[habitIndex]
                2 -> userDataViewModel.habits[habitIndex].copy()
                3 -> userDataViewModel.habitTemplates[habitIndex]
                else -> Habit()
            }
            ProjectP2Theme(userDataViewModel.theme) { DetailsScreen(userDataViewModel, navController, drawerState, scope, habitType, habit) }
        }
        composable("stats") { ProjectP2Theme(userDataViewModel.theme) { StatisticsScreen(userDataViewModel, navController, drawerState, scope) } }

        composable("info") { ProjectP2Theme(userDataViewModel.theme) { InfoScreen(userDataViewModel, navController, drawerState, scope) } }
        composable("settings/{setting}") { backStackEntry ->
            val setting = backStackEntry.arguments?.getString("setting").toString()
            ProjectP2Theme(userDataViewModel.theme) { SettingsScreen(userDataViewModel, navController, drawerState, scope, setting) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (nestedScrollConnection: NestedScrollConnection) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),

        floatingActionButton = { floatingActionButton() },

        topBar = {
            TopAppBar(
                title = {
                    Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimary)
                },

                navigationIcon = {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } }
                    ) {
                        Icon(Icons.Default.Menu, "More", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },

                actions = { MenuItems(navController) },

                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),

                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues)) {
            content(scrollBehavior.nestedScrollConnection)
        }
    }
}

@Composable
fun DrawerContent(userDataViewModel: UserDataViewModel, navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val screenRoutes = listOf("home", "habits", "stats")
    val screenTitles = listOf("Home", "Habits", "Statistics")
    val screenIcons = listOf(Icons.Default.Home, Icons.Default.Build, Icons.AutoMirrored.Filled.List, Icons.Default.Star, Icons.Default.Star)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    ProjectP2Theme(userDataViewModel.theme) {
        ModalDrawerSheet(
            modifier = Modifier.width(300.dp),
            drawerContainerColor = MaterialTheme.colorScheme.background,
            drawerContentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Column(
                modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { scope.launch { drawerState.close() } }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                    Spacer(Modifier.width(16.dp))

                    Text(stringResource(id = R.string.app_name), fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)

                    Image(
                        painter = painterResource(R.drawable.app_icon_nobg),
                        contentDescription = "App icon",
                        modifier = Modifier.size(64.dp)
                    )
                }

                HorizontalDivider(Modifier.padding(vertical = 8.dp))

                for (index in screenRoutes.indices) {
                    currentRoute?.contains(screenRoutes[index])?.let {
                        NavigationDrawerItem(
                            icon = { Icon(screenIcons[index], contentDescription = screenTitles[index]) },
                            label = { Text(text = screenTitles[index], fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp)) },
                            selected = it,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(screenRoutes[index])
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItems(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Row {
        IconButton(
            onClick = { if (currentRoute != "info") navController.navigate("info") }
        ) {
            Icon(Icons.Default.Info, "Info", tint = MaterialTheme.colorScheme.onPrimary)
        }

        IconButton(
            onClick = { if (!currentRoute!!.contains("settings")) navController.navigate("settings/theme") }
        ) {
            Icon(Icons.Default.Settings, "Settings", tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun AddNewFAB(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("details/0/0") },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(50)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(24.dp))
    }
}
