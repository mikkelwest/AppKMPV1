package com.example.kmptemplateappv1.presentation.profile

object CounterContract {

    data class State(
        val count: Int = 0,
        val isLoading: Boolean = false
    )


    // Event/Intent
    sealed interface Event {
        data object IncrementClicked : Event
        data object DecrementClicked : Event
        data object ResetClicked : Event
    }

    /**
     * One-off UI events: snackbars, navigation, toasts, etc.
     */
    sealed interface Effect {
        data class ShowMessage(val text: String) : Effect
    }
}