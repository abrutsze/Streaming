package com.android.streaming.data.repository

import com.android.database.api.ViewerDao
import com.android.dispatchers.api.DispatchersProvider
import com.android.models.UiViewer
import com.android.network.api.ViewerApi
import com.android.streaming.data.mapper.toEntity
import com.android.streaming.data.mapper.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single(binds = [ViewerRepository::class])
internal class ViewerRepositoryImpl(
    private val viewerDao: ViewerDao,
    private val viewerApi: ViewerApi,
    private val dispatchersProvider: DispatchersProvider
) : ViewerRepository {

    override fun observeViewers(): Flow<List<UiViewer>> {
        return viewerDao.observeViewers()
            .map { entities -> entities.map { it.toUiModel() } }
            .flowOn(dispatchersProvider.io)
    }

    override suspend fun refresh(limit: Int) = withContext(dispatchersProvider.io) {
        val response = viewerApi.fetchViewers(limit)
        val viewers = response.results.map { it.toEntity() }
        viewerDao.clear()
        viewerDao.insertAll(viewers)
    }
}

