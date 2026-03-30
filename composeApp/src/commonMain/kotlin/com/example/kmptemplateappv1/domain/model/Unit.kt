package com.example.kmptemplateappv1.domain.model
import com.example.kmptemplateappv1.data.networking.Response.GroupResponse
import com.example.kmptemplateappv1.data.networking.Response.UnitResponse
import kotlin.text.toInt

data class Unit(
    val id: Int,
    val assetsid: Int,
    val assetsGuid: String? = null,
    val name: String? = null,
    val value: Int? = null,
    val assetkey: String? = null,
    val timeStamp: String? = null
)

fun UnitResponse.toGroup(): Unit {
    return Unit(
        id = id,
        assetsid = assetsid,
        assetsGuid = assetsGuid,
        name = name,
        value = value,
        assetkey = assetkey,
        timeStamp = timeStamp
    )
}
fun Unit.toGroupResponse(): UnitResponse {
    return UnitResponse(
        id = id,
        assetsid = assetsid,
        assetsGuid = assetsGuid,
        name = name,
        value = value,
        assetkey = assetkey,
        timeStamp = timeStamp
    )
}