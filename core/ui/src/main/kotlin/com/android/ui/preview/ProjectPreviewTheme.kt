package com.android.ui.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.android.ui.theme.AppStyle
import com.android.ui.theme.ProjectTheme
import com.android.ui.theme.AppColors

@Composable
fun ProjectPreviewTheme(content: @Composable () -> Unit) {
    ProjectTheme(style = if (isSystemInDarkTheme()) AppStyle.DARK else AppStyle.LIGHT) {
        Surface(
            color = AppColors.transparent
        ) {
            content()
        }
    }
}

@Composable
fun ProjectPreviewComponentTheme(content: @Composable () -> Unit) {
    ProjectTheme(style = if (isSystemInDarkTheme()) AppStyle.DARK else AppStyle.LIGHT) {
        content()
    }
}
