package com.android.network.api

import com.android.response.RandomUserResponse

interface ViewerApi {

    suspend fun fetchViewers(limit: Int): RandomUserResponse
}