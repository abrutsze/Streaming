package com.android.network.impl.services

import com.android.network.api.ViewerApi
import com.android.response.RandomUserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Single

@Single
class ViewerImpl(
    private val httpClient: HttpClient
) : ViewerApi {

    companion object {
        private const val DEFAULT_LIMIT = 20
        private const val INCLUDED_FIELDS = "name,login,picture,location"
    }

    override suspend fun fetchViewers(limit: Int ): RandomUserResponse {
        return httpClient.get() {
            parameter("results", limit)
            parameter("inc", INCLUDED_FIELDS)
        }.body()
    }
}
