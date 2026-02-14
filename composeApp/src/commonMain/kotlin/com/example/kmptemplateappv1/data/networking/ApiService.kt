package com.example.kmptemplateappv1.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

/**
 * Multiplatform API Service using Ktor
 * This replaces the OpenAPI-generated JVM-only client
 */
class ApiService(
    private val client: HttpClient,
    private val baseUrl: String = "https://poset-api.fly.dev/api/v1"
) {

    // ==================== Asset Config Int Value ====================

    /**
     * POST /asset/configintvalue
     */
    suspend fun postAssetConfigIntValue(request: PostAssetIntValueRequest): ApiResult<Unit> {
        return safeApiCall {
            client.post("$baseUrl/asset/configintvalue") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    /**
     * GET /asset/{asset_id}/configintvalue
     */
    suspend fun getAssetConfigIntValue(assetId: Int): ApiResult<Unit> {
        return safeApiCall {
            client.get("$baseUrl/asset/$assetId/configintvalue")
        }
    }

    /**
     * PUT /asset/{asset_id}/configintvalue
     */
    suspend fun putAssetConfigIntValue(assetId: Int, request: PutAssetIntValueRequest): ApiResult<Unit> {
        return safeApiCall {
            client.put("$baseUrl/asset/$assetId/configintvalue") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    /**
     * DELETE /asset/{asset_id}/configintvalue
     */
    suspend fun deleteAssetConfigIntValue(assetId: Int): ApiResult<Unit> {
        return safeApiCall {
            client.delete("$baseUrl/asset/$assetId/configintvalue")
        }
    }

    // ==================== Units ====================

    /**
     * GET /units/{asset_guid}
     */
    suspend fun getUnits(assetGuid: String): ApiResult<Unit> {
        return safeApiCall {
            client.get("$baseUrl/units/$assetGuid")
        }
    }

    /**
     * PUT /units/{asset_guid}
     */
    suspend fun putUnits(assetGuid: String, request: PutAssetIntValueFromUnitRequest): ApiResult<Unit> {
        return safeApiCall {
            client.put("$baseUrl/units/$assetGuid") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    /**
     * GET /units/{asset_id}/key/{key}
     */
    suspend fun getUnitsByKey(assetId: Int, key: String): ApiResult<Unit> {
        return safeApiCall {
            client.get("$baseUrl/units/$assetId/key/$key")
        }
    }

    /**
     * GET /units/datetime/day/minutes
     */
    suspend fun getDatetimeDayMinutes(): ApiResult<Unit> {
        return safeApiCall {
            client.get("$baseUrl/units/datetime/day/minutes")
        }
    }

    // ==================== Version ====================

    /**
     * GET /version
     */
    suspend fun getVersion(): ApiResult<String> {
        return safeApiCall {
            val response = client.get("$baseUrl/version")
            response.bodyAsText()
        }
    }

    // ==================== Helper ====================

    private suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): ApiResult<T> {
        return try {
            ApiResult.Success(block())
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }
}

// ==================== Result Wrapper ====================

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

// ==================== Request/Response Models ====================

@Serializable
data class PostAssetIntValueRequest(
    val assetId: Int? = null,
    val key: String? = null,
    val value: Int? = null
)

@Serializable
data class PutAssetIntValueRequest(
    val key: String? = null,
    val value: Int? = null
)

@Serializable
data class PutAssetIntValueFromUnitRequest(
    val key: String? = null,
    val value: Int? = null
)

