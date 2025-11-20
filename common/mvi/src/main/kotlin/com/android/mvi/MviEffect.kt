package com.android.mvi

import com.android.screens.Screens

interface MviEffect {
    data class Navigate(val screen: Screens) : MviEffect
    data class OnErrorDialog(val error: String) : MviEffect

}