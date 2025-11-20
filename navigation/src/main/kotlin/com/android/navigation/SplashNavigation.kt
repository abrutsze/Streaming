package com.android.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.android.customSplash.CustomSplashRoute
import com.android.navigation.NavigationManager.navigateTo
import com.android.screens.AppScreens

fun NavGraphBuilder.splashNavigation(rootNavController: NavHostController) {

    composable<AppScreens.SplashScreen> { _ ->
        CustomSplashRoute(onNavigate = {
            rootNavController.navigateTo(it)
        })
    }
}

