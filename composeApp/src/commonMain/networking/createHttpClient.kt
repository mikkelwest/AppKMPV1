package networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging


fun createHttpClient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
        }
        /*
        install(contentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        install(authentication) {
            bearer {
                loadTokens {
                    BearerTokens( accessToken = "your_access_token", refreshToken = "your_refresh_token" )
                }
            }
        }
        */

        // Configure your HTTP client here (e.g., timeouts, logging, etc.)
    }
}