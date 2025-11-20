package com.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable

internal val roundShapes = Shapes(
    circle = RoundedCornerShape(100),
    rectangle = RoundedCornerShape(0),
)

@Stable
data class Shapes(
    val circle: RoundedCornerShape,
    val rectangle: RoundedCornerShape,
)
