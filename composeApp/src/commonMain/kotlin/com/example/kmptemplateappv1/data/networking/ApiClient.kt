package com.example.kmptemplateappv1.data.networking

import com.example.kmptemplateappv1.data.networking.Request.UnitConfigRequest
import com.example.kmptemplateappv1.data.networking.Response.GroupResponse
import com.example.kmptemplateappv1.data.networking.Response.UnitIntHistotoryResponse
import com.example.kmptemplateappv1.data.networking.Response.UnitResponse
import com.example.kmptemplateappv1.domain.model.UnitConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.koin.core.scope.ScopeID
import util.NetworkError
import util.Result
import kotlin.math.log

class ApiClient(private val httpClient: HttpClient) {
    suspend fun unitConfig(unitNumber: Int ): Result<List<UnitResponse>, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "https://api.sdlab.dk/asset/$unitNumber/configintvalue"
            ) {
                accept(ContentType.Application.Json)
            }
        } catch(e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch(e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return when(response.status.value) {
            in 200..299 -> {
                val response = response.body<List<UnitResponse>>()
                Result.Success(response)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    suspend fun unitIntHistory(unitGuid: String ): Result<List<UnitIntHistotoryResponse>, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "https://api.sdlab.dk/unitsIntHistory/$unitGuid/count/10"
                //urlString = "https://api.sdlab.dk/unitsIntHistory/1c1e3fbd-afb1-4579-8779-eee2b07d9c80/count/10"
            ) {
                accept(ContentType.Application.Json)
            }
        } catch(e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch(e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return when(response.status.value) {
            in 200..299 -> {
                try {
                    val response = response.body<List<UnitIntHistotoryResponse>>()
                    Result.Success(response)
                } catch (e: SerializationException) {
                    Result.Error(NetworkError.SERIALIZATION)
                }
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }


    suspend fun saveUnitConfig(assetID: Int, unit: UnitConfig): Result<String, NetworkError> {

        var UnitConfigRequest = UnitConfigRequest(
            name = unit.name,
            value = unit.value,
            assetsGuid = unit.assetsGuid,
            assetkey = unit.assetkey,
            isEditable = unit.isEditable
        )


        val response = try {
            httpClient.put(
                urlString = "https://api.sdlab.dk/asset/$assetID/configintvalue"
            ) {
                contentType(ContentType.Application.Json)
                setBody(UnitConfigRequest)
            }
        } catch(e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch(e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return when(response.status.value) {
            in 200..299 -> {
                try {
                    val response = response.body<String>()
                    Result.Success(response)
                } catch (e: SerializationException) {
                    Result.Error(NetworkError.SERIALIZATION)
                }
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }



}