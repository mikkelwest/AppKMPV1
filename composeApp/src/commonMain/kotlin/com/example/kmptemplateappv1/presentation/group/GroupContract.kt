package com.example.kmptemplateappv1.presentation.login

sealed interface GroupContract {

    data class State(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed interface Event {
        data class OnEmailChanged(val email: String) : Event
    }

    sealed interface Effect {
        data object NavigateToHome : Effect
    }
}