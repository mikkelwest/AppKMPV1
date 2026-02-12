package com.example.kmptemplateappv1.presentation.login

sealed interface LoginContract {

    data class State(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed interface Event {
        data class OnEmailChanged(val email: String) : Event
        data class OnPasswordChanged(val password: String) : Event
        data object OnLoginClicked : Event
        data object OnRegisterClicked : Event
    }

    sealed interface Effect {
        data object NavigateToHome : Effect
        data object NavigateToRegister : Effect
        data class ShowError(val message: String) : Effect
    }
}