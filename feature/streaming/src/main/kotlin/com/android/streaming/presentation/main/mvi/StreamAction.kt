package com.android.streaming.presentation.main.mvi

import com.android.models.UiViewer
import com.android.mvi.MviAction
import com.android.streaming.presentation.main.model.CameraFacing
import com.android.streaming.presentation.main.model.StreamStatus

sealed class StreamAction : MviAction {
    data class SetPermissions(val granted: Boolean) : StreamAction()
    data class SetStatus(val status: StreamStatus) : StreamAction()
    data class SetDuration(val seconds: Long) : StreamAction()
    data class SetMicMuted(val muted: Boolean) : StreamAction()
//    data class SetStreaming(val streaming: Boolean) : StreamAction()
    data class SetCameraFacing(val facing: CameraFacing) : StreamAction()
    data class SetStreamKey(val key: String) : StreamAction()
    data class SetViewers(val viewers: List<UiViewer>) : StreamAction()
    data class SetSearchQuery(val query: String, val filtered: List<UiViewer>) : StreamAction()
    data class SetBottomSheetVisible(val visible: Boolean) : StreamAction()
    data class SetRefreshing(val refreshing: Boolean) : StreamAction()
    data class SetError(val error: String?) : StreamAction()
}

