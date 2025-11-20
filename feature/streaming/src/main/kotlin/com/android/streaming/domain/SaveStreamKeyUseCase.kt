package com.android.streaming.domain

import com.android.datastore.api.DataStoreRepository
import com.android.dispatchers.api.DispatchersProvider
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

interface SaveStreamKeyUseCase {
    suspend operator fun invoke(streamKey: String)
}

@Factory
internal class SaveStreamKeyUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatchersProvider: DispatchersProvider
) : SaveStreamKeyUseCase {
    override suspend fun invoke(streamKey: String) = withContext(dispatchersProvider.io) {
        dataStoreRepository.streamKey = streamKey
    }
}

