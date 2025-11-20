package com.android.streaming.presentation.main.mvi

import com.android.mvi.MviIntent

sealed class StreamIntent : MviIntent {
    data object Initialize : StreamIntent()
    data class PermissionsResult(val granted: Boolean) : StreamIntent()
    data object StartStream : StreamIntent()
    data object StopStream : StreamIntent()
    data object ToggleMic : StreamIntent()
    data object SwitchCamera : StreamIntent()
    data object ShowViewers : StreamIntent()
    data object HideViewers : StreamIntent()
    data class UpdateSearch(val query: String) : StreamIntent()
    data object RefreshViewers : StreamIntent()
    data object OnStreamingConnected : StreamIntent()
    data object OnStreamingDisconnected : StreamIntent()
    data class OnStreamingFailed(val reason: String) : StreamIntent()
    data object OnStreamingRetrying : StreamIntent()
}

