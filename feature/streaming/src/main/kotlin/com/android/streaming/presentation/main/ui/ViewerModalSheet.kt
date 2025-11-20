package com.android.streaming.presentation.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.models.UiViewer
import com.android.resources.R
import com.android.streaming.presentation.main.mvi.StreamIntent
import com.android.streaming.presentation.main.mvi.StreamState
import com.android.ui.theme.AppColors
import com.android.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerModalSheet(
    state: StreamState,
    bottomSheetState: SheetState,
    onIntent: (StreamIntent) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onIntent(StreamIntent.HideViewers) },
        sheetState = bottomSheetState
    ) {
        ViewerListContent(state = state, onIntent = onIntent)
    }
}


@Composable
private fun ViewerListContent(
    state: StreamState,
    onIntent: (StreamIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.stream_main_viewers_title),
                style = AppTypography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onIntent(StreamIntent.RefreshViewers) }) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = stringResource(id = R.string.stream_main_viewers_refresh)
                )
            }
        }

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { onIntent(StreamIntent.UpdateSearch(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.stream_main_viewers_search_hint)) },
            singleLine = true
        )

        if (state.isRefreshingViewers) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (state.filteredViewers.isEmpty()) {
            Text(
                text = stringResource(id = R.string.stream_main_viewers_empty),
                style = AppTypography.bodyMedium,
                color = AppColors.grey800
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = state.filteredViewers, key = { it.id }) { viewer ->
                    ViewerItem(viewer = viewer)
                }
            }
        }
    }
}

@Composable
private fun ViewerItem(
    viewer: UiViewer
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = viewer.avatarUrl,
            contentDescription = viewer.name,
            modifier = Modifier
                .size(48.dp)
                .background(color = AppColors.grey300, shape = CircleShape)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = viewer.name, style = AppTypography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(
                text = "@${viewer.username}",
                style = AppTypography.bodySmall,
                color = AppColors.grey800
            )
            if (viewer.location.isNotBlank()) {
                Text(
                    text = viewer.location,
                    style = AppTypography.bodySmall,
                    color = AppColors.grey800
                )
            }
        }
    }
}