package com.android.streaming.presentation.main.engine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun rememberStreamEngine(
    onEvent: (StreamEngineEvent) -> Unit
): StreamEngine {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val eventHandler = rememberUpdatedState(onEvent)
    val engine = remember {
        StreamEngine(context) { event -> eventHandler.value.invoke(event) }
    }

    DisposableEffect(lifecycleOwner, engine) {
        lifecycleOwner.lifecycle.addObserver(engine)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(engine)
            engine.release()
        }
    }
    return engine
}

