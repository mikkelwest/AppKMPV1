package com.example.kmptemplateappv1.presentation.group

import com.example.kmptemplateappv1.domain.model.Group
import com.example.kmptemplateappv1.domain.model.UnitConfig

sealed interface GroupContract {

    data class State(
        val groups: List<Group> = emptyList(),
        var selectedGroup: Int = 1,
        val isLoading: Boolean = false,
        val error: String? = null,
        val unitList : List<Unit> = emptyList(),
        val unitConfig : UnitConfig? = null
    )

    sealed interface Event {
        data class OnGroupSelected(val groupId: Int) : Event

        data object OnBackClicked : Event

        data class OnUpdateUnits(val unitId: Int, val unitConfig : UnitConfig ) : Event

    }

    sealed class Effect {
        data class NavigateToGroup(val groupId: Int) : Effect()

        data object NavigateBack : Effect()
    }
}