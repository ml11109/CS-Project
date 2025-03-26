package com.example.projectp2.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.projectp2.AppScaffold
import com.example.projectp2.data.AppViewModel

@Composable
fun HomeScreen(appViewModel: AppViewModel, navController: NavController) {
    val context = LocalContext.current

    AppScaffold (
        "CS Project", navController
    ) { nestedScrollConnection ->
        Column(
            modifier = Modifier.nestedScroll(nestedScrollConnection)
        ) {
            /*
            InfoBar()
            Spacer(Modifier.size(16.dp))
            MiniStatsScreen()
             */
        }
    }
}
