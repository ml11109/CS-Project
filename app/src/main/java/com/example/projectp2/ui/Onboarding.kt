package com.example.projectp2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectp2.R
import com.example.projectp2.composables.PagerScreen

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    PagerScreen(4) { page ->
        val screenModifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        when (page) {
            0 -> { Page1(screenModifier) }
            1 -> { Page2(screenModifier) }
            2 -> { Page3(screenModifier) }
            3 -> { Page4(onComplete, screenModifier) }
        }
    }
}

@Composable
fun Page1(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.app_icon_nobg),
            contentDescription = null,
            modifier = Modifier.size(300.dp)
        )

        CustomText(
            text = "Welcome to",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        CustomText(
            text = "Habit Studio!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(64.dp))
    }
}

@Composable
fun Page2(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomText(
            text = "Habit Studio is an app designed to help users build a healthy lifestyle.",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(16.dp))

        CustomText(
            text = "Through records, statistics, and a small gamification element, we'll help encourage you to maintain good habits!",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Page3(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.home_page),
            contentDescription = null,
            modifier = Modifier.size(350.dp)
        )
        Spacer(Modifier.size(32.dp))

        CustomText(
            text = "Click the menu icon in the top left to navigate to other pages, " +
                    "and click the + button in the bottom right to add a new habit.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(16.dp))

        CustomText(
            text = "Each habit will have a number of tasks automatically generated, " +
                    "which you'll need to complete.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(16.dp))

        CustomText(
            text = "Keep working on your habits every day, and you'll even get some achievements!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(32.dp))
    }
}

@Composable
fun Page4(onComplete: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(start = 48.dp, end = 48.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomText(
            text = "That's all for now. Click the button below to get started...",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(16.dp))
        CustomText(
            text = "and best of luck with improving your life!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.size(24.dp))

        Button(
            onClick = onComplete
        ) { Text("Let's go!", style = MaterialTheme.typography.titleLarge) }
    }
}

@Composable
fun CustomText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        style = style,
        textAlign = textAlign,
        color = color
    )
}
