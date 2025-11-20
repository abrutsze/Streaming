package com.android.screens

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
interface Screens {
    @Stable
    @Serializable
    data class Back(
        val payload: Map<String, String>? = null
    ) : Screens

    @Stable
    @Serializable
    data class BackTo(
        val screen: Screens,
        val exclusive:Boolean = false,
        val payload: Map<String, String>? = null
    ) : Screens

    @Stable
    @Serializable
    data class NavigateToRoot(
        val screen: Screens,
        val exclusive:Boolean = false
    ) : Screens
}