package com.example.projectp2.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.projectp2.model.UserDataViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun StatsScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope, ) {

}

@Composable
fun MiniStatsScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Stats")
    }
}

@Composable
fun MiniAchievementsScreen(userDataViewModel: UserDataViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Achievements")
    }
}
