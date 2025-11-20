package com.android.models

import androidx.compose.runtime.Immutable

@Immutable
data class UiViewer(
    val id: String,
    val name: String,
    val username: String,
    val avatarUrl: String,
    val location: String
)

