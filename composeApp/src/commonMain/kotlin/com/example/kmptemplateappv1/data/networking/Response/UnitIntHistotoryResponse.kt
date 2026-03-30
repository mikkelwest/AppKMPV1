package com.example.kmptemplateappv1.data.networking.Response

import kotlinx.serialization.Serializable

@Serializable
data class UnitIntHistotoryResponse (
    val id: Int,
    val value: Int? = null,
    val timeStamp: String? = null
)