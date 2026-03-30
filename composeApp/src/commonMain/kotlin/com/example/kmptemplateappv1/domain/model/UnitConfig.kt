package com.example.kmptemplateappv1.domain.model


data class UnitConfig(
    val name: String? = null,
    var value: Int = 0,
    val assetsGuid: String? = null,
    val assetkey: String? = null,
    val isEditable: Boolean? = true,
)