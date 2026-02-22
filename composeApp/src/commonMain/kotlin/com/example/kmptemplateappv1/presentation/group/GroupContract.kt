package com.example.kmptemplateappv1.presentation.group

import com.example.kmptemplateappv1.domain.model.Group

sealed interface GroupContract {

    data class State(
        val groups: List<Group> = emptyList(),
        val selectedGroup: Group? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        data class OnGroupSelected(val group: Group) : Event
    }

    sealed interface Effect {
        //data object LoadGroups : Effect
        data class LoadGroup(val groupNumber: Int) : Effect
    }
}