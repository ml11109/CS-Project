package com.example.projectp2.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.projectp2.data.AppViewModel
import com.example.projectp2.utils.CollapsingToolbarScaffold

@Composable
fun HomeScreen(appViewModel: AppViewModel, navController: NavController) {
    val context = LocalContext.current

    CollapsingToolbarScaffold("CS Project") {
        // Content
    }
}
