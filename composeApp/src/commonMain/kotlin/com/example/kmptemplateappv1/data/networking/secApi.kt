package com.example.kmptemplateappv1.data.networking

import com.example.kmptemplateappv1.data.repository.tokenStore
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * NOTE: The OpenAPI you provided doesn't show token fields in the `/token` response.
 * I'm making a reasonable assumption: `/token` and `/admin/refreshtoken` returns
 * an object containing `accessToken` and `refreshToken`. If your backend uses a
 * different shape, adjust the `TokenResponse` model accordingly.
 */

@Serializable
data class PostNewUserRequest(
    val email: String? = null,
    val password: String? = null
)

@Serializable
data class PostRefreshTokenRequest(
    val email: String? = null,
    val refreshToken: String? = null
)

@Serializable
data class ResponseModel(
    val responseText: String? = null,
    val responseCode: Int? = null
)

@Serializable
data class TokenResponse(
    val access_token: String? = null,
    val refresh_token: String? = null,
    val responseText: String? = null,
    val responseCode: Int? = null
)

class SecApi(private val client: HttpClient, private val baseUrl: String) {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Call POST /token to obtain tokens. Saves tokens to the shared tokenStore when present.
     * Returns the TokenResponse if the server returned token fields, otherwise decodes a
     * generic ResponseModel and returns null.
     */
    suspend fun login(email: String, password: String): TokenResponse? {
        val req = PostNewUserRequest(email = email, password = password)
        val resp = client.post("$baseUrl/token") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(req)
        }
        val raw = resp.bodyAsText()

        // Try decoding as TokenResponse first
        return try {
            val tok = json.decodeFromString<TokenResponse>(raw)
            if (!tok.access_token.isNullOrBlank() && !tok.refresh_token.isNullOrBlank()) {
                tokenStore.setTokens(tok.access_token, tok.refresh_token)
            }
            tok
        } catch (e: Exception) {
            // Fallback: try decode generic response
            try {
                json.decodeFromString<ResponseModel>(raw)
                // no tokens present
                null
            } catch (_: Exception) {
                // Could not decode; rethrow the original exception for visibility
                throw e
            }
        }
    }

    /**
     * Call POST /admin/refreshtoken with email + refreshToken to get new tokens.
     * Will update tokenStore on success.
     */
    suspend fun refresh(email: String, refreshToken: String): TokenResponse? {
        val req = PostRefreshTokenRequest(email = email, refreshToken = refreshToken)
        val resp = client.post("$baseUrl/admin/refreshtoken") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(req)
        }
        val raw = resp.bodyAsText()
        return try {
            val tok = json.decodeFromString<TokenResponse>(raw)
            if (!tok.access_token.isNullOrBlank() && !tok.refresh_token.isNullOrBlank()) {
                tokenStore.setTokens(tok.access_token, tok.refresh_token)
            }
            tok
        } catch (e: Exception) {
            null
        }
    }
}
