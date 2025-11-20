package com.android.network.impl.services

interface UrlProvider {
    fun getProtocol(): String
    fun getUrl(): String
}