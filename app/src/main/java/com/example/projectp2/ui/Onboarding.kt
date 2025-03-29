package com.example.projectp2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projectp2.R
import com.example.projectp2.composables.PagerScreen

@Composable
fun OnboardingScreen(navController: NavController) {
    PagerScreen(2) { page ->
        when (page) {
            0 -> { Page1() }
            1 -> { Page2(navController) }
            // Add more pages here
        }
    }
}

@Composable
fun Page1() {
    Column(
        modifier = Modifier.padding(32.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painterResource(R.drawable.sample_image), contentDescription = null)
        Spacer(Modifier.size(32.dp))
        Text(
            text = "", // Insert text here
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Page2(navController: NavController) {
    Column(
        modifier = Modifier.padding(32.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "", // Insert text here
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(24.dp))
        Button(
            onClick = {
                navController.navigate("main") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        ) { Text("Continue") }
    }
}