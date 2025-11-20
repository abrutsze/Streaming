package com.android.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

internal val lightColors = Colors(
    primary = Color(0xFFFF0237),
    white = Color(0xFFFFFFFF),
    black = Color(0xFF000000),
    transparent = Color(0x00000000),
    grey900 = Color(0xFF242E42),
    grey800 = Color(0xFF77757A),
    grey300 = Color(0xFFE4E4E5),
    brightRed = Color(0xFFD23330),
    green = Color(0xFF34C759),
    yellow = Color(0xFFF2C94C),
    yellow200 = Color(0xFFFFA726),
)

internal val darkColors = Colors(
    primary = Color(0xFFFFFFFF),
    white = Color(0xFFFFFFFF),
    black = Color(0xFF000000),
    transparent = Color(0x00000000),
    grey900 = Color(0xFF242E42),
    grey800 = Color(0xFF77757A),
    grey300 = Color(0xFFE4E4E5),
    brightRed = Color(0xFFD23330),
    green = Color(0xFF34C759),
    yellow = Color(0xFFF2C94C),
    yellow200 = Color(0xFFFFA726),
)

@Stable
data class Colors(
    val primary: Color,
    val white: Color,
    val black: Color,
    val transparent: Color,
    val grey900: Color,
    val grey800: Color,
    val grey300: Color,
    val brightRed: Color,
    val green: Color,
    val yellow: Color,
    val yellow200: Color,
)