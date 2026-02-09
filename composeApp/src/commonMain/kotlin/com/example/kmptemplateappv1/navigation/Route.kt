package com.example.kmptemplateappv1.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object  TodoList : Route,  NavKey

    @Serializable
    data class TodoDetails(val todo: String) : Route, NavKey

    @Serializable
    data object ToLogin : Route, NavKey

    @Serializable
    data object ToProfile : Route, NavKey

    @Serializable
    data object ToControl : Route, NavKey

    @Serializable
    data object ToGroupList : Route, NavKey


}