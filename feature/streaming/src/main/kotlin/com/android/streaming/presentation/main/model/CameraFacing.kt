package com.android.streaming.presentation.main.model

enum class CameraFacing {
    FRONT, BACK;

    fun toggle(): CameraFacing = if (this == FRONT) BACK else FRONT
}

