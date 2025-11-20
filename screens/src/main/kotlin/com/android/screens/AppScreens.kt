package com.android.screens

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed class AppScreens : Screens {

    @Stable
    @Serializable
    data object SplashScreen : AppScreens()

    @Stable
    @Serializable
    data object StreamConfigScreen : AppScreens()

    @Stable
    @Serializable
    data object StreamMainScreen : AppScreens()

}
