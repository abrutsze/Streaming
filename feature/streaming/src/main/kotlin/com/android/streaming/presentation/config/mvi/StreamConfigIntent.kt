package com.android.streaming.presentation.config.mvi

import com.android.mvi.MviIntent

sealed class StreamConfigIntent : MviIntent {
    data class OnStreamKeyChanged(val value: String) : StreamConfigIntent()
    data object OnSaveClicked : StreamConfigIntent()
    data object LoadStreamKey : StreamConfigIntent()
}

