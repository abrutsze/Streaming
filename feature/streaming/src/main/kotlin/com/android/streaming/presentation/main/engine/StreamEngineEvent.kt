package com.android.streaming.presentation.main.engine

sealed class StreamEngineEvent {
    data object Connected : StreamEngineEvent()
    data object Disconnected : StreamEngineEvent()
    data class Failed(val reason: String) : StreamEngineEvent()
}

