package com.android.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
val AppColors: Colors
    @Composable get() = LocalCurrentColor.current

@Stable
val AppTypography: Typography
    @Composable get() = LocalAppTypography.current

@Stable
val shapes: Shapes
    @Composable get() = LocalAppRoundedCornerShape.current


internal val LocalCurrentColor = staticCompositionLocalOf<Colors> {
    error("No colors provided")
}
internal val LocalAppTypography = staticCompositionLocalOf<Typography> {
    error("No font provided")
}
internal val LocalAppRoundedCornerShape = staticCompositionLocalOf<Shapes> {
    error("No shape provided")
}