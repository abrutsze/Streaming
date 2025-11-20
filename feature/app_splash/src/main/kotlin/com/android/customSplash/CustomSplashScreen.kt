package com.android.customSplash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.screens.Screens
import com.android.ui.config.SystemBarConfig
import com.android.ui.preview.ProjectPreviewTheme
import com.android.ui.preview.ProjectPreviews
import com.android.ui.theme.AppColors
import com.android.screens.AppScreens
import com.android.resources.R


@Composable
fun CustomSplashRoute(
    onNavigate: (Screens) -> Unit
) {

    LaunchedEffect(Unit) {
        onNavigate(Screens.NavigateToRoot(AppScreens.StreamConfigScreen))
    }

    SystemBarConfig(
        statusBarColor = AppColors.primary,
        isFullScreen = true,
        isIconBarVisible = true,
        isEdgeToEdgeEnabled = false,
        isDarkIcons = false
    )
    SplashScreen()
}

@Composable
internal fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.white),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
@ProjectPreviews
private fun SplashScreenPreview() {
    ProjectPreviewTheme {
        SplashScreen()
    }
}


