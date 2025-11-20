package com.android.network.impl.di


import com.android.network.impl.services.UrlProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.android.network.impl")
class DataModule {


    @Single
    fun provideJson() = Json {
        isLenient = true
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = false
        prettyPrint = true
        coerceInputValues = true
        encodeDefaults = true
        allowStructuredMapKeys = true
        explicitNulls = true
    }

    @Single
    fun provideKtor(json: Json, urlProvider: UrlProvider): HttpClient =
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(
                    json = json,
                    contentType = ContentType.Application.Json
                )
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HttpClient: $message")
                    }
                }
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.createOrDefault(urlProvider.getProtocol())
                    host = urlProvider.getUrl()
                }
                contentType(ContentType.Application.Json)
            }
            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, _ ->
                    when (cause) {
                        is JsonConvertException -> cause.printStackTrace()
                        else -> throw cause
                    }
                }
            }
        }

}
