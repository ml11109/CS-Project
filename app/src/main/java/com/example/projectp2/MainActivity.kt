package com.example.projectp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectp2.data.AppViewModel
import com.example.projectp2.ui.HomeScreen
import com.example.projectp2.ui.OnboardingScreen
import com.example.projectp2.ui.SplashScreen

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

    NavHost(navController, startDestination = "main") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("main") { HomeScreen(appViewModel, navController) }
    }
}
