package com.android.streaming.presentation.config.mvi

import com.android.mvi.MviAction

sealed class StreamConfigAction : MviAction {
    data class UpdateStreamKey(val value: String) : StreamConfigAction()
}

