package com.android.streaming.domain

import com.android.streaming.data.repository.ViewerRepository
import org.koin.core.annotation.Factory

interface RefreshViewersUseCase {
    suspend operator fun invoke(limit: Int = 20)
}

@Factory
internal class RefreshViewersUseCaseImpl(
    private val repository: ViewerRepository
) : RefreshViewersUseCase {
    override suspend fun invoke(limit: Int) {
        repository.refresh(limit)
    }
}

