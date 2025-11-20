package com.android.streaming.presentation.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.mvi.MviEffect
import com.android.screens.Screens
import com.android.streaming.presentation.config.mvi.StreamConfigIntent
import com.android.streaming.presentation.config.mvi.StreamConfigState
import com.android.ui.components.buttons.PrimaryButton
import com.android.ui.components.dialogs.LoadingDialog
import com.android.ui.config.SystemBarConfig
import com.android.ui.preview.ProjectPreviewTheme
import com.android.ui.theme.AppColors
import com.android.ui.theme.AppTypography
import com.android.resources.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun StreamConfigRoute(
    onNavigate: (Screens) -> Unit
) {
    val viewModel: StreamConfigViewModel = koinViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is MviEffect.Navigate -> onNavigate(effect.screen)
                is MviEffect.OnErrorDialog -> scope.launch {
                    snackbarHostState.showSnackbar(effect.error)
                }
            }
        }
    }

    StreamConfigScreen(
        state = viewModel.viewState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@Composable
internal fun StreamConfigScreen(
    state: StreamConfigState,
    snackbarHostState: SnackbarHostState,
    onIntent: (StreamConfigIntent) -> Unit
) {
    SystemBarConfig(
        statusBarColor =AppColors.primary,
        isFullScreen = false
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.stream_config_title),
                style = AppTypography.titleMedium,
            )
            Text(
                text = stringResource(id = R.string.stream_config_description),
                style = AppTypography.bodyMedium,
            )
            OutlinedTextField(
                value = state.streamKey,
                onValueChange = { onIntent(StreamConfigIntent.OnStreamKeyChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.stream_config_hint)) },
            )
            Text(
                text = stringResource(id = R.string.stream_config_example),
                style = AppTypography.bodySmall,
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                text = stringResource(id = R.string.stream_config_save),
                onClick = { onIntent(StreamConfigIntent.OnSaveClicked) },
                isEnabled = state.isSaveEnabled
            )
            Spacer(modifier = Modifier.height(12.dp))
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StreamConfigPreview() {
    ProjectPreviewTheme {
        StreamConfigScreen(
            state = StreamConfigState(streamKey = "live_123"),
            snackbarHostState = SnackbarHostState(),
            onIntent = {}
        )
    }
}

