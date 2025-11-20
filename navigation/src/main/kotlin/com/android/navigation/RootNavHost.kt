package com.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.android.screens.AppScreens
import com.android.screens.Screens

@Composable
fun RootNavHost(
    modifier: Modifier = Modifier,
    startDestination: Screens = AppScreens.SplashScreen
) {
    val rootNavController: NavHostController = rememberNavController()
    NavHost(
        navController = rootNavController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        splashNavigation(rootNavController)
        streamNavigation(rootNavController)
    }
}
