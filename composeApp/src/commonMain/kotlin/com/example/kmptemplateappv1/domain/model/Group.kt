package com.example.kmptemplateappv1.domain.model

import com.example.kmptemplateappv1.data.networking.Response.GroupResponse

data class Group(
    val groupId: Long,
    val groupName: String? = null,
    val timeStamp: String,
    val assetId: List<Int?> = emptyList(),
    val subGroups: List<Group> = emptyList()
)

// Extension function to convert GroupResponse to Group (domain model)
fun GroupResponse.toGroup(): Group {
    return Group(
        groupId = groupId,
        groupName = groupName,
        timeStamp = timeStamp,
        assetId = assetId.map { it?.toInt() },
        subGroups = subGroups.map { it.toGroup() }
    )
}

// Extension function to convert Group back to GroupResponse
fun Group.toGroupResponse(): GroupResponse {
    return GroupResponse(
        groupId = groupId,
        groupName = groupName,
        timeStamp = timeStamp,
        assetId = assetId.map { it?.toInt() },
        subGroups = subGroups.map { it.toGroupResponse() }
    )
}

// Extension function to convert a list of GroupResponse to list of Group
fun List<GroupResponse>.toGroupList(): List<Group> = map { it.toGroup() }

// Extension function to convert a list of Group to list of GroupResponse
fun List<Group>.toGroupResponseList(): List<GroupResponse> = map { it.toGroupResponse() }
