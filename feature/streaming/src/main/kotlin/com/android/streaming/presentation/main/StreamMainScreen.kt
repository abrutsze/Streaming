package com.android.streaming.presentation.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cameraswitch
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.android.mvi.MviEffect
import com.android.resources.R
import com.android.screens.Screens
import com.android.streaming.presentation.main.engine.StreamEngine
import com.android.streaming.presentation.main.engine.StreamEngineEvent
import com.android.streaming.presentation.main.engine.rememberStreamEngine
import com.android.streaming.presentation.main.model.StreamStatus
import com.android.streaming.presentation.main.mvi.StreamEffect
import com.android.streaming.presentation.main.mvi.StreamIntent
import com.android.streaming.presentation.main.mvi.StreamState
import com.android.streaming.presentation.main.ui.ViewerModalSheet
import com.android.streaming.presentation.main.util.formatDuration
import com.android.ui.components.card.PermissionCard
import com.android.ui.config.SystemBarConfig
import com.android.ui.theme.AppColors
import com.android.ui.theme.AppTypography
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun StreamMainRoute(
    onNavigate: (Screens) -> Unit
) {
    val viewModel: StreamViewModel = koinViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        viewModel.onIntent(StreamIntent.PermissionsResult(granted))
    }

    val streamEngine = rememberStreamEngine { event ->
        when (event) {
            StreamEngineEvent.Connected -> viewModel.onIntent(StreamIntent.OnStreamingConnected)
            StreamEngineEvent.Disconnected -> viewModel.onIntent(StreamIntent.OnStreamingDisconnected)
            is StreamEngineEvent.Failed -> viewModel.onIntent(StreamIntent.OnStreamingFailed(event.reason))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is MviEffect.Navigate -> onNavigate(effect.screen)
                is MviEffect.OnErrorDialog -> scope.launch { snackbarHostState.showSnackbar(effect.error) }
                is StreamEffect.StartPreview -> streamEngine.startPreview(effect.cameraFacing)
                is StreamEffect.StartStreaming -> streamEngine.startStream(effect.streamKey)
                StreamEffect.StopStreaming -> streamEngine.stopStream()
                is StreamEffect.SetMicEnabled -> streamEngine.setMicEnabled(effect.enabled)
                StreamEffect.SwitchCamera -> streamEngine.switchCamera()
                is StreamEffect.ShowMessage -> scope.launch { snackbarHostState.showSnackbar(effect.message) }
                StreamEffect.RequestPermissions -> permissionsLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                )
            }
        }
    }

    StreamMainScreen(
        state = viewModel.viewState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
        streamEngine = streamEngine
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StreamMainScreen(
    state: StreamState,
    snackbarHostState: SnackbarHostState,
    onIntent: (StreamIntent) -> Unit,
    streamEngine: StreamEngine
) {
    SystemBarConfig(statusBarColor = Color.Transparent, isFullScreen = true, isDarkIcons = false)

    Box(modifier = Modifier.fillMaxSize()) {
        StreamingPreview(streamEngine = streamEngine, modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AppColors.black.copy(alpha = 0.5f),
                            Color.Transparent,
                            AppColors.black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StreamInfoPanel(
                status = state.status,
                duration = formatDuration(state.durationSeconds),
                viewerCount = state.viewerCount,
                isOnline = state.status == StreamStatus.ONLINE,
                onClick = { if (state.status == StreamStatus.ONLINE) onIntent(StreamIntent.ShowViewers) }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ControlButton(
                    status = state.status,
                    onStart = { onIntent(StreamIntent.StartStream) },
                    onStop = { onIntent(StreamIntent.StopStream) }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RoundControlButton(
                        icon = if (state.isMicMuted) Icons.Rounded.MicOff else Icons.Rounded.Mic,
                        label = if (state.isMicMuted) stringResource(id = R.string.stream_main_mic_off) else stringResource(
                            id = R.string.stream_main_mic_on
                        ),
                        selected = state.isMicMuted
                    ) {
                        onIntent(StreamIntent.ToggleMic)
                    }
                    RoundControlButton(
                        icon = Icons.Rounded.Cameraswitch,
                        label = stringResource(id = R.string.stream_main_camera_switch),
                        selected = false
                    ) {
                        onIntent(StreamIntent.SwitchCamera)
                    }
                }
            }
        }

        if (!state.isPermissionsGranted) {
            PermissionCard(
                modifier = Modifier.align(Alignment.Center),
                message = stringResource(id = R.string.stream_main_permissions_required)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }

    if (state.isBottomSheetVisible) {
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ViewerModalSheet(state, bottomSheetState, onIntent)
    }
}

@Composable
private fun StreamingPreview(
    streamEngine: StreamEngine,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { streamEngine.getPreviewView() },
        modifier = modifier
    )
}

@Composable
private fun StreamInfoPanel(
    status: StreamStatus,
    duration: String,
    viewerCount: Int,
    isOnline: Boolean,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.black.copy(alpha = 0.35f),
            contentColor = AppColors.white
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isOnline, onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusDot(status = status)
                Text(
                    text = statusLabel(status),
                    style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                if(status == StreamStatus.ONLINE) {
                    Icon(imageVector = Icons.Rounded.Visibility, contentDescription = null)
                    Text(
                        text = "$viewerCount",
                        style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${stringResource(id = R.string.stream_main_duration)}: $duration",
                style = AppTypography.bodySmall
            )
        }
    }
}

@Composable
private fun StatusDot(status: StreamStatus) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(
                color = when (status) {
                    StreamStatus.OFFLINE -> AppColors.grey800
                    StreamStatus.CONNECTING -> AppColors.yellow
                    StreamStatus.ONLINE -> AppColors.green
                    StreamStatus.RECONNECTING -> AppColors.yellow200
                },
                shape = CircleShape
            )
    )
}

@Composable
private fun ControlButton(
    status: StreamStatus,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    val isActive = status == StreamStatus.CONNECTING || status == StreamStatus.ONLINE
    val text = if (isActive) stringResource(id = R.string.stream_main_stop) else stringResource(id = R.string.stream_main_start)
    val icon = if (isActive) Icons.Rounded.Stop else Icons.Rounded.PlayArrow
    val buttonColor = if (isActive) AppColors.brightRed else AppColors.primary
    Button(
        onClick = { if (isActive) onStop() else onStart() },
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = text, style = AppTypography.bodyMedium.copy(color = AppColors.white))
    }
}

@Composable
private fun RoundControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (selected) AppColors.black.copy(alpha = 0.6f) else AppColors.black.copy(alpha = 0.4f),
            contentColor = AppColors.white
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = AppTypography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



@Composable
private fun statusLabel(status: StreamStatus): String {
    return when (status) {
        StreamStatus.OFFLINE -> stringResource(id = R.string.stream_main_status_offline)
        StreamStatus.CONNECTING -> stringResource(id = R.string.stream_main_status_connecting)
        StreamStatus.ONLINE -> stringResource(id = R.string.stream_main_status_online)
        StreamStatus.RECONNECTING -> stringResource(id = R.string.stream_main_status_reconnecting)
    }
}

