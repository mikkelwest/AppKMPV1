package com.example.kmptemplateappv1.data.networking.Response

import kotlinx.serialization.Serializable

@Serializable
data class GroupResponse(
    val groupId: Long,
    val groupName: String? = null,
    val timeStamp: String,
    val subGroups: List<GroupResponse> = emptyList()
)