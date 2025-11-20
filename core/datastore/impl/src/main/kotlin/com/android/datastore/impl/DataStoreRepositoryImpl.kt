package com.android.datastore.impl

import com.android.datastore.api.DataStoreRepository
import org.koin.core.annotation.Single

@Single
class DataStoreRepositoryImpl(private val dataStoreService: DataStoreService) : DataStoreRepository {

    override var streamKey: String
        get() = dataStoreService.streamKey
        set(value) {
            dataStoreService.streamKey = value
        }
}
