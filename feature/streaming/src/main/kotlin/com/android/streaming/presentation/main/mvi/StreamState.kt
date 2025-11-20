package com.android.streaming.presentation.main.mvi

import com.android.models.UiViewer
import com.android.mvi.MviState
import com.android.streaming.presentation.main.model.CameraFacing
import com.android.streaming.presentation.main.model.StreamStatus

data class StreamState(
    val streamKey: String = "",
    val status: StreamStatus = StreamStatus.OFFLINE,
    val durationSeconds: Long = 0L,
    val isMicMuted: Boolean = false,
    val cameraFacing: CameraFacing = CameraFacing.BACK,
    val isPermissionsGranted: Boolean = false,
    val viewers: List<UiViewer> = emptyList(),
    val filteredViewers: List<UiViewer> = emptyList(),
    val searchQuery: String = "",
    val isBottomSheetVisible: Boolean = false,
    val isRefreshingViewers: Boolean = false,
    val errorMessage: String? = null
) : MviState {

    val viewerCount: Int = filteredViewers.size

    val canStartStream: Boolean
        get() = streamKey.isNotBlank() && status != StreamStatus.CONNECTING
}

