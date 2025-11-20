package com.android.streaming.presentation.config.mvi

import com.android.mvi.MviState

data class StreamConfigState(
    val streamKey: String = ""
) : MviState {

    val isSaveEnabled: Boolean get() = streamKey.isNotBlank()
}

