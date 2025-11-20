package com.android.tar

import com.android.network.impl.services.UrlProvider
import com.android.streaming.BuildConfig
import org.koin.core.annotation.Single

@Single
class UrlProviderImpl(): UrlProvider {
    override fun getProtocol(): String  = BuildConfig.PROTOCOL
    override fun getUrl(): String = BuildConfig.BASE_URL
}
