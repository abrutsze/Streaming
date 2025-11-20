package com.android.mvi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class MviBaseViewModel<S : MviState, A : MviAction, I : MviIntent>(initialState: S, reducer: Reducer<A, S>) :
    ViewModel() {

    var viewState by mutableStateOf(initialState)

    private val actions = MutableSharedFlow<A>()

    private val _effects = Channel<MviEffect>(capacity = Channel.CONFLATED)
    val effects = _effects.receiveAsFlow()

    init {
        actions.onEach { action ->
           // _viewState.update { reducer.reduce(action, it) }
            viewState = reducer.reduce(action, viewState)
        }.launchIn(viewModelScope)
    }

    protected abstract fun handleIntent(intent: I)

    fun onIntent(intent: I) {
        handleIntent(intent)
    }

    protected fun onAction(action: A) {
        viewModelScope.launch {
            actions.emit(action)
        }
    }

    protected fun onEffect(effect: MviEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}


