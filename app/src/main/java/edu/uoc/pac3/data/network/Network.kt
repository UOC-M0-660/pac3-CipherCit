package edu.uoc.pac3.data.network

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

/**
 * Created by alex on 07/09/2020.
 */
object Network {

    private const val TAG = "Network"

    fun createHttpClient(context: Context): HttpClient {
        return HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }

            defaultRequest {
                accept(ContentType.Application.Json)
            }
        }
    }
}

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = false
}