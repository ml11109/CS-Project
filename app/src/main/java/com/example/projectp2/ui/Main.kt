package com.example.projectp2.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.projectp2.data.AppViewModel
import com.example.projectp2.utils.ToolbarScaffold

@Composable
fun MainScreen(appViewModel: AppViewModel, navController: NavController) {
    val context = LocalContext.current

    ToolbarScaffold("ICA 2") {
        // Content
    }
}
