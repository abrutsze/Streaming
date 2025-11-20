package com.android.ui.config

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.android.ui.preview.ProjectPreviewTheme
import com.android.ui.theme.AppColors

@SuppressLint("WrongConstant")
@Composable
fun SystemBarConfig(
    statusBarColor: Color = AppColors.white,
    navigationBarColor: Color = statusBarColor,
    // If true, enables full-screen mode (navigation bar hidden)
    isFullScreen: Boolean = false,
    // If true, status bar is visible with icons in full-screen mode; if false, status bar is hidden
    isIconBarVisible: Boolean = false,
    // If true, enables edge-to-edge layout
    isEdgeToEdgeEnabled: Boolean = false,
    // If true, uses dark icons (for light backgrounds); if false, uses light icons
    isDarkIcons: Boolean = false
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    // Apply edge-to-edge configuration if activity is available
    activity?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val window = it.window
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            // Handle Status Bar and Edge-to-Edge
            if (isFullScreen && !isIconBarVisible) {
                // Hide status bar for full-screen with no icons
                insetsController.hide(android.view.WindowInsets.Type.statusBars())
                // Apply edge-to-edge with transparent status bar
                it.enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        Color.Transparent.toArgb(),
                        Color.Transparent.toArgb()
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        Color.Transparent.toArgb(),
                        Color.Transparent.toArgb()
                    )
                )
            } else {
                // Show status bar (in full-screen with icons or non-full-screen)
                insetsController.show(android.view.WindowInsets.Type.statusBars())
                // Apply edge-to-edge or colored status bar
                it.enableEdgeToEdge(
                    statusBarStyle = if (isEdgeToEdgeEnabled) {
                        SystemBarStyle.light(
                            Color.Transparent.toArgb(),
                            Color.Transparent.toArgb()
                        )
                    } else {
                        if (isDarkIcons) {
                            SystemBarStyle.light(
                                statusBarColor.toArgb(),
                                statusBarColor.toArgb()
                            )
                        } else {
                            SystemBarStyle.dark(statusBarColor.toArgb())
                        }
                    },
                    navigationBarStyle = if (isEdgeToEdgeEnabled) {
                        SystemBarStyle.light(
                            Color.Transparent.toArgb(),
                            Color.Transparent.toArgb()
                        )
                    } else {
                        if (isDarkIcons) {
                            SystemBarStyle.light(
                                navigationBarColor.toArgb(),
                                navigationBarColor.toArgb()
                            )
                        } else {
                            SystemBarStyle.dark(navigationBarColor.toArgb())
                        }
                    }
                )
                insetsController.isAppearanceLightStatusBars = isDarkIcons
            }

            // Handle Navigation Bar
            if (isFullScreen) {
                // Hide navigation bar in full-screen
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                insetsController.hide(android.view.WindowInsets.Type.navigationBars())
            } else {
                // Show navigation bar
                insetsController.show(android.view.WindowInsets.Type.navigationBars())
                insetsController.isAppearanceLightNavigationBars = isDarkIcons
            }

            // Configure edge-to-edge layout
            WindowCompat.setDecorFitsSystemWindows(window, !isEdgeToEdgeEnabled && !isFullScreen)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SystemBarConfigPreview() {
    ProjectPreviewTheme {
        SystemBarConfig(
            statusBarColor = AppColors.primary,       // Status bar background color
            navigationBarColor = AppColors.white,   // Navigation bar color (can be any color or transparent)
            isFullScreen = true,                    // Hide the navigation bar
            isIconBarVisible = true,                // Show the status bar with icons
            isEdgeToEdgeEnabled = false,            // Disable edge-to-edge (if not needed)
            isDarkIcons = false                      // false = use light icons , true = use dark icons
        )
    }
}
