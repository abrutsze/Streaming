package com.android.streaming.presentation.main.util

import java.util.concurrent.TimeUnit

fun formatDuration(totalSeconds: Long): String {
    if (totalSeconds <= 0) return "00:00"
    val hours = TimeUnit.SECONDS.toHours(totalSeconds)
    val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

