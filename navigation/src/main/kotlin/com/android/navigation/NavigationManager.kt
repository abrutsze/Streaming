package com.android.navigation

import androidx.navigation.NavController
import com.android.screens.Screens

object NavigationManager {

    private fun NavController.setBackStackEntry(payload:Map<String, String>?){
        payload?.entries?.forEach { entry ->
            currentBackStackEntry?.savedStateHandle?.set(
                entry.key,
                entry.value
            )
        }
    }

    fun NavController.navigateTo(screen: Screens) {
        when (screen) {
            is Screens.Back -> {
                popBackStack()
                setBackStackEntry(screen.payload)
            }

            is Screens.BackTo -> {
                popBackStack(route = screen.screen, false)
                setBackStackEntry(screen.payload)
            }
            is Screens.NavigateToRoot -> {
                navigate(screen.screen){
                    popUpTo(0) { inclusive = screen.exclusive }
                }
            }
            else -> {
                navigate(screen)
            }
        }
    }
}


