package com.android.streaming.domain

import com.android.models.UiViewer
import com.android.streaming.data.repository.ViewerRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface ObserveViewersUseCase {
    operator fun invoke(): Flow<List<UiViewer>>
}

@Factory
internal class ObserveViewersUseCaseImpl(
    private val repository: ViewerRepository
) : ObserveViewersUseCase {
    override fun invoke(): Flow<List<UiViewer>> = repository.observeViewers()
}

