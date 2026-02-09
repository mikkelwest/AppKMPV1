package com.example.kmptemplateappv1.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun createPlatformHttpClient(): HttpClient = HttpClient(Darwin) {
    expectSuccess = true
    install(Logging) {
        level = LogLevel.ALL
    }
    install(Auth) {
        bearer {
            loadTokens {
                val access = tokenStore.getAccessToken()
                val refresh = tokenStore.getRefreshToken()
                return@loadTokens if (access != null && refresh != null) {
                    BearerTokens(accessToken = access, refreshToken = refresh)
                } else {
                    null
                }
            }
        }
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true; isLenient = true })
    }
}

