package com.example.kmptemplateappv1.data.networking.Request

import kotlinx.serialization.Serializable

@Serializable
data class UnitConfigRequest(
    val name: String? = null,
    val value: Int? = null,
    val assetsGuid: String? = null,
    val assetkey: String? = null,
    val isEditable: Boolean ? = null
)
