package networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


fun createHttpClient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                // Ktor calls this from a coroutine context, so we can call suspend functions.
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
        /*
        install(contentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        */



        // Configure your HTTP client here (e.g., timeouts, logging, etc.)
    }
}

