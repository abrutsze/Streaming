package com.android.streaming.domain

import com.android.datastore.api.DataStoreRepository
import com.android.dispatchers.api.DispatchersProvider
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

interface GetStreamKeyUseCase {
    suspend operator fun invoke(): String
}

@Factory
internal class GetStreamKeyUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatchersProvider: DispatchersProvider
) : GetStreamKeyUseCase {
    override suspend fun invoke(): String = withContext(dispatchersProvider.io) {
        dataStoreRepository.streamKey
    }
}

