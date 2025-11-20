package com.android.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.android.navigation.NavigationManager.navigateTo
import com.android.screens.AppScreens
import com.android.streaming.presentation.config.StreamConfigRoute
import com.android.streaming.presentation.main.StreamMainRoute

fun NavGraphBuilder.streamNavigation(rootNavController: NavHostController) {
    composable<AppScreens.StreamConfigScreen> {
        StreamConfigRoute(onNavigate = { screen -> rootNavController.navigateTo(screen) })
    }

    composable<AppScreens.StreamMainScreen> {
        StreamMainRoute(onNavigate = { screen -> rootNavController.navigateTo(screen) })
    }
}

