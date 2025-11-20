package com.android.streaming.presentation.config.mvi

import com.android.mvi.Reducer
import org.koin.core.annotation.Single

@Single
class StreamConfigReducer : Reducer<StreamConfigAction, StreamConfigState> {

    override fun reduce(action: StreamConfigAction, state: StreamConfigState): StreamConfigState {
        return when (action) {
            is StreamConfigAction.UpdateStreamKey -> state.copy(
                streamKey = action.value
            )
        }
    }
}

