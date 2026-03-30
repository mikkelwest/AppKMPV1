package com.example.kmptemplateappv1.data.networking.Response

import kotlinx.serialization.Serializable

@Serializable
data class UnitResponse (
    val id: Int,
    val assetsid: Int,
    val assetsGuid: String? = null,
    val name: String? = null,
    val value: Int? = null,
    val assetkey: String? = null,
    val timeStamp: String? = null,
    val isEditable: Boolean? = null
)