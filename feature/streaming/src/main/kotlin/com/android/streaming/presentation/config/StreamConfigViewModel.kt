package com.android.streaming.presentation.config

import androidx.lifecycle.viewModelScope
import com.android.mvi.MviBaseViewModel
import com.android.mvi.MviEffect
import com.android.streaming.domain.GetStreamKeyUseCase
import com.android.streaming.domain.SaveStreamKeyUseCase
import com.android.streaming.presentation.config.mvi.StreamConfigAction
import com.android.streaming.presentation.config.mvi.StreamConfigIntent
import com.android.streaming.presentation.config.mvi.StreamConfigReducer
import com.android.streaming.presentation.config.mvi.StreamConfigState
import com.android.screens.AppScreens
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class StreamConfigViewModel(
    private val saveStreamKeyUseCase: SaveStreamKeyUseCase,
    private val getStreamKeyUseCase: GetStreamKeyUseCase,
    reducer: StreamConfigReducer
) : MviBaseViewModel<StreamConfigState, StreamConfigAction, StreamConfigIntent>(
    initialState = StreamConfigState(),
    reducer = reducer
) {

    init {
        onIntent(StreamConfigIntent.LoadStreamKey)
    }

    override fun handleIntent(intent: StreamConfigIntent) {
        when (intent) {
            StreamConfigIntent.LoadStreamKey -> preloadStreamKey()
            StreamConfigIntent.OnSaveClicked -> saveStreamKey()
            is StreamConfigIntent.OnStreamKeyChanged -> handleStreamKeyChanged(intent.value)
        }
    }

    private fun preloadStreamKey() {
        viewModelScope.launch {
            val saved = getStreamKeyUseCase()
            onAction(StreamConfigAction.UpdateStreamKey(saved))
        }
    }

    private fun handleStreamKeyChanged(value: String) {
        val sanitized = value.trim()
        onAction(StreamConfigAction.UpdateStreamKey(sanitized))
    }

    private fun saveStreamKey() {
        val currentKey = viewState.streamKey
        viewModelScope.launch {
            saveStreamKeyUseCase(currentKey)
            onEffect(MviEffect.Navigate(AppScreens.StreamMainScreen))
        }
    }
}

