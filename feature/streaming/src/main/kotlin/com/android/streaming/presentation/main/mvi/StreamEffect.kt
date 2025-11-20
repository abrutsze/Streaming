package com.android.streaming.presentation.main.mvi

import com.android.mvi.MviEffect
import com.android.streaming.presentation.main.model.CameraFacing

sealed class StreamEffect : MviEffect {
    data class StartPreview(val cameraFacing: CameraFacing) : StreamEffect()
    data class StartStreaming(val streamKey: String) : StreamEffect()
    data object StopStreaming : StreamEffect()
    data class SetMicEnabled(val enabled: Boolean) : StreamEffect()
    data object SwitchCamera : StreamEffect()
    data class ShowMessage(val message: String) : StreamEffect()
    data object RequestPermissions : StreamEffect()
}

