package com.example.projectp2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projectp2.model.UserDataViewModel
import com.example.projectp2.util.sendEmail
import kotlinx.coroutines.CoroutineScope

@Composable
fun InfoScreen(userDataViewModel: UserDataViewModel, navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    val context = LocalContext.current

    AppScaffold(
        title = "Info",
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) { nestedScrollConnection ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Habit Studio:",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "A Habit Tracking App",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "This app is designed to help users build a healthy lifestyle as a tool to " +
                        "keep track of their habits. Through records, statistics, and a small " +
                        "gamification element, users are encouraged to maintain good habits.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "For queries or feedback, click below to contact us:",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

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
                Text("Send Email", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
