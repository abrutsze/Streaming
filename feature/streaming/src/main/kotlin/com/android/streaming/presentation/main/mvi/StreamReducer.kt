package com.android.streaming.presentation.main.mvi

import com.android.models.UiViewer
import com.android.mvi.Reducer

internal class StreamReducer : Reducer<StreamAction, StreamState> {
    override fun reduce(action: StreamAction, state: StreamState): StreamState {
        return when (action) {
            is StreamAction.SetPermissions -> state.copy(isPermissionsGranted = action.granted)
            is StreamAction.SetStatus -> state.copy(status = action.status)
            is StreamAction.SetDuration -> state.copy(durationSeconds = action.seconds)
            is StreamAction.SetMicMuted -> state.copy(isMicMuted = action.muted)
            is StreamAction.SetCameraFacing -> state.copy(cameraFacing = action.facing)
            is StreamAction.SetStreamKey -> state.copy(streamKey = action.key)
            is StreamAction.SetViewers -> state.copy(
                viewers = action.viewers,
                filteredViewers = filterViewers(action.viewers, state.searchQuery)
            )
            is StreamAction.SetSearchQuery -> state.copy(
                searchQuery = action.query,
                filteredViewers = action.filtered
            )
            is StreamAction.SetBottomSheetVisible -> state.copy(isBottomSheetVisible = action.visible)
            is StreamAction.SetRefreshing -> state.copy(isRefreshingViewers = action.refreshing)
            is StreamAction.SetError -> state.copy(errorMessage = action.error)
        }
    }

    private fun filterViewers(viewers: List<UiViewer>, query: String): List<UiViewer> {
        if (query.isBlank()) return viewers
        val lowerQuery = query.lowercase()
        return viewers.filter {
            it.name.lowercase().contains(lowerQuery) ||
                it.username.lowercase().contains(lowerQuery) ||
                it.location.lowercase().contains(lowerQuery)
        }
    }
}

