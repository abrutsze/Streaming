package com.android.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

val TextStyle.regular: TextStyle
    get() = copy()

val TextStyle.emphasized_W500: TextStyle
    get() = copy(fontWeight =  FontWeight.W500)

val TextStyle.emphasized_W600: TextStyle
    get() = copy(fontWeight = FontWeight.W600)

val TextStyle.emphasized_W700: TextStyle
    get() = copy(fontWeight = FontWeight.W700)

val TextStyle.italic: TextStyle
    get() = copy(fontStyle = FontStyle.Italic)

