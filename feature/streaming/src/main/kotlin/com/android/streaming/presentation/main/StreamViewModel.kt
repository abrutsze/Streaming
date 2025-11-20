package com.android.streaming.presentation.main

import androidx.lifecycle.viewModelScope
import com.android.models.UiViewer
import com.android.mvi.MviBaseViewModel
import com.android.mvi.MviEffect
import com.android.screens.AppScreens
import com.android.streaming.domain.GetStreamKeyUseCase
import com.android.streaming.domain.ObserveViewersUseCase
import com.android.streaming.domain.RefreshViewersUseCase
import com.android.streaming.presentation.main.model.CameraFacing
import com.android.streaming.presentation.main.model.StreamStatus
import com.android.streaming.presentation.main.mvi.StreamAction
import com.android.streaming.presentation.main.mvi.StreamEffect
import com.android.streaming.presentation.main.mvi.StreamIntent
import com.android.streaming.presentation.main.mvi.StreamReducer
import com.android.streaming.presentation.main.mvi.StreamState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class StreamViewModel(
    private val getStreamKeyUseCase: GetStreamKeyUseCase,
    private val observeViewersUseCase: ObserveViewersUseCase,
    private val refreshViewersUseCase: RefreshViewersUseCase
) : MviBaseViewModel<StreamState, StreamAction, StreamIntent>(
    initialState = StreamState(),
    reducer = StreamReducer()
) {

    private var timerJob: Job? = null

    init {
        onIntent(StreamIntent.Initialize)
    }

    override fun handleIntent(intent: StreamIntent) {
        when (intent) {
            StreamIntent.Initialize -> initialize()
            is StreamIntent.PermissionsResult -> handlePermissions(intent.granted)
            StreamIntent.StartStream -> startStreaming()
            StreamIntent.StopStream -> stopStreaming()
            StreamIntent.ToggleMic -> toggleMic()
            StreamIntent.SwitchCamera -> switchCamera()
            StreamIntent.ShowViewers -> showViewers()
            StreamIntent.HideViewers -> onAction(StreamAction.SetBottomSheetVisible(false))
            is StreamIntent.UpdateSearch -> updateSearch(intent.query)
            StreamIntent.RefreshViewers -> refreshViewers()
            StreamIntent.OnStreamingConnected -> onStreamingConnected()
            StreamIntent.OnStreamingDisconnected -> onStreamingDisconnected()
            is StreamIntent.OnStreamingFailed -> onStreamingFailed(intent.reason)
            StreamIntent.OnStreamingRetrying -> onAction(StreamAction.SetStatus(StreamStatus.RECONNECTING))
        }
    }

    private fun initialize() {
        viewModelScope.launch {
            val savedKey = getStreamKeyUseCase()
            if (savedKey.isBlank()) {
                onEffect(MviEffect.Navigate(AppScreens.StreamConfigScreen))
            } else {
                onAction(StreamAction.SetStreamKey(savedKey))
            }
        }
        observeViewers()
        onEffect(StreamEffect.RequestPermissions)

    }

    private fun handlePermissions(granted: Boolean) {
        onAction(StreamAction.SetPermissions(granted))
        if (granted) {
            onEffect(StreamEffect.StartPreview(viewState.cameraFacing))
        } else {
            onEffect(StreamEffect.ShowMessage("Camera and microphone permissions are required."))
        }
    }

    private fun startStreaming() {
        val currentState = viewState
        if (!currentState.isPermissionsGranted) {
            onEffect(StreamEffect.ShowMessage("Grant permissions to start a stream."))
            return
        }
        if (currentState.status == StreamStatus.CONNECTING) {
            return
        }
        if (!currentState.canStartStream) {
            onEffect(StreamEffect.ShowMessage("Please provide a valid stream key in the configuration screen."))
            onEffect(MviEffect.Navigate(AppScreens.StreamConfigScreen))
            return
        }
        onAction(StreamAction.SetStatus(StreamStatus.CONNECTING))
        onAction(StreamAction.SetDuration(0))
        onEffect(StreamEffect.StartStreaming(currentState.streamKey))
    }

    private fun stopStreaming() {
        stopTimer()
        onAction(StreamAction.SetStatus(StreamStatus.OFFLINE))
        onAction(StreamAction.SetDuration(0))
        onEffect(StreamEffect.StopStreaming)
    }

    private fun toggleMic() {
        val newMuted = !viewState.isMicMuted
        onAction(StreamAction.SetMicMuted(newMuted))
        onEffect(StreamEffect.SetMicEnabled(enabled = !newMuted))
    }

    private fun switchCamera() {
        val newFacing = viewState.cameraFacing.toggle()
        onAction(StreamAction.SetCameraFacing(newFacing))
        onEffect(StreamEffect.SwitchCamera)
    }

    private fun showViewers() {
        onAction(StreamAction.SetBottomSheetVisible(true))
    }

    private fun updateSearch(query: String) {
        val filtered = filterViewers(viewState.viewers, query)
        onAction(StreamAction.SetSearchQuery(query, filtered))
    }

    private fun onStreamingConnected() {
        onAction(StreamAction.SetStatus(StreamStatus.ONLINE))
        startTimer()
        refreshViewers()
    }

    private fun onStreamingDisconnected() {
        stopTimer()
        onAction(StreamAction.SetStatus(StreamStatus.OFFLINE))
    }

    private fun onStreamingFailed(reason: String) {
        stopStreaming()
        onEffect(StreamEffect.ShowMessage(reason))
    }

    private fun observeViewers() {
        viewModelScope.launch {
            observeViewersUseCase().collectLatest { viewers ->
                onAction(StreamAction.SetViewers(viewers))
            }
        }
    }

    private fun refreshViewers() {
        viewModelScope.launch {
            onAction(StreamAction.SetRefreshing(true))
            try {
                refreshViewersUseCase()
            } catch (error: Throwable) {
                onEffect(StreamEffect.ShowMessage(error.message.orEmpty()))
            } finally {
                onAction(StreamAction.SetRefreshing(false))
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var seconds = 0L
            while (true) {
                delay(1_000)
                seconds += 1
                onAction(StreamAction.SetDuration(seconds))
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        onAction(StreamAction.SetDuration(0))
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

