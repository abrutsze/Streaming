package com.android.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.resources.R


internal val fontsRoboto = FontFamily(
    Font(R.font.roboto_bold, weight = FontWeight.W700),
    Font(R.font.roboto_medium, weight = FontWeight.W500),
    Font(R.font.roboto_regular, weight = FontWeight.W400),
    Font(R.font.roboto_light, weight = FontWeight.W300),
    Font(R.font.roboto_black, weight = FontWeight.W900),
    Font(R.font.roboto_thin, weight = FontWeight.W100),
)

internal val localTypography
    @Composable get() = Typography(
        titleLarge = TextStyle(
            fontFamily = fontsRoboto,
            fontWeight = FontWeight.W400,
            fontSize = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = fontsRoboto,
            fontWeight = FontWeight.W400,
            fontSize = 22.sp
        ),
        titleSmall = TextStyle(
            fontFamily = fontsRoboto,
            fontWeight = FontWeight.W400,
            fontSize = 20.sp
        ),
        bodyLarge = TextStyle(
            fontSize = 18.sp,
            fontFamily = fontsRoboto,
            fontWeight = FontWeight.W400,
        ),
        bodyMedium = TextStyle(
            fontSize = 14.sp,
            fontFamily = fontsRoboto,
            fontWeight = FontWeight.W400,
        ),
        bodySmall = TextStyle(
            fontSize = 12.sp,
            fontFamily = fontsRoboto,
            fontWeight = FontWeight.W400
        )
    )

@Stable
data class Typography(
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle
)