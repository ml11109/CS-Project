package com.example.projectp2.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/*
// Screen with row of tabs above
TabScreen(
    numTabs = 2,
    tabTitles = listOf("Tab 1", "Tab 2"),
    tabIcons = listOf(Icons.AutoMirrored.Filled.List, Icons.Default.Person)
) { tabIndex ->
    when (tabIndex) {
        0 -> Tab1(viewModel, navController)
        1 -> Tab2(viewModel, navController)
        // Add more tabs here
    }
}
 */

@Composable
fun TabScreen(numTabs: Int, tabTitles: List<String> = emptyList(), tabIcons: List<ImageVector> = emptyList(), getTab: @Composable (Int) -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val filledTabTitles = tabTitles + List((numTabs - tabTitles.size).coerceAtLeast(0)) { "Tab ${it + 1}" }
    val filledTabIcons = tabIcons + List((numTabs - tabIcons.size).coerceAtLeast(0)) { Icons.Default.Info }

    Column {
        TabRow(tabIndex) {
            for (index in 0 until numTabs) {
                if (tabTitles.isEmpty() && tabIcons.isEmpty()) {
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                } else if (tabTitles.isEmpty()) {
                    Tab(
                        icon = { Icon(filledTabIcons[index], null) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                } else if (tabIcons.isEmpty()) {
                    Tab(
                        text = { Text(filledTabTitles[index]) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                } else {
                    Tab(
                        text = { Text(filledTabTitles[index]) },
                        icon = { Icon(filledTabIcons[index], null) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
        }

        getTab(tabIndex)
    }
}


/*
// HorizontalPager with indicator
PagerScreen(2) { page ->
    when (page) {
        0 -> { Page1() }
        1 -> { Page2(navController) }
        // Add more pages here
    }
}
// And then define each page individually (see Onboarding.kt)
 */

@Composable
fun PagerScreen(numPages: Int, getPage: @Composable (page: Int) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { numPages })
    HorizontalPager(state = pagerState) { page -> getPage(page) }
    PageIndicator(pagerState)
}

@Composable
fun PageIndicator(pagerState: PagerState) {
    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 64.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
fun ScreenSwitcher(
    numScreens: Int,
    initialScreen: Int = 0,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    getScreen: @Composable (screen: Int) -> Unit
) {
    var screen by remember { mutableIntStateOf(initialScreen) }

    Box(
        modifier = modifier
    ) {
        getScreen(screen)

        IconButton(
            onClick = { screen = (screen + 1) % numScreens },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
        }

        IconButton(
            onClick = { screen = (screen - 1 + numScreens) % numScreens },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous")
        }
    }
}
