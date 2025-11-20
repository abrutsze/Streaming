package com.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun ProjectTheme(
    style: AppStyle = AppStyle.LIGHT,
    content: @Composable () -> Unit
) {
    val colors = when (style) {
        AppStyle.LIGHT -> lightColors
        AppStyle.DARK -> darkColors
        AppStyle.SYSTEM -> if (isSystemInDarkTheme()) darkColors
        else lightColors
    }

    CompositionLocalProvider(
        LocalCurrentColor provides colors,
        LocalAppTypography provides localTypography,
        LocalAppRoundedCornerShape provides roundShapes,
        content = content
    )
}