package com.android.streaming.data.repository

import com.android.models.UiViewer
import kotlinx.coroutines.flow.Flow

interface ViewerRepository {
    fun observeViewers(): Flow<List<UiViewer>>
    suspend fun refresh(limit: Int = 20)
}

