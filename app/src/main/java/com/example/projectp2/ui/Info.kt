package com.example.projectp2.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.util.sendEmail
import kotlinx.coroutines.CoroutineScope

@Composable
fun InfoScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
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
            val context = LocalContext.current

            Text("hi")
            Button(
                onClick = {
                    sendEmail(
                        context,
                        "leejhmalcolm@gmail.com",
                        "Android App Query",
                        "Please enter your query here:"
                    )
                }
            ) {
                Text("Send Email")
            }
        }
    }
}
