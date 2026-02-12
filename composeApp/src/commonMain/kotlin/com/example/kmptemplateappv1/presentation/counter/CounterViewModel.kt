package com.example.kmptemplateappv1.presentation.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {

    private val _state = MutableStateFlow(CounterContract.State())
    val state: StateFlow<CounterContract.State> = _state

    private val _effect = Channel<CounterContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: CounterContract.Event) {
        when (event) {
            CounterContract.Event.IncrementClicked -> increment()
            CounterContract.Event.DecrementClicked -> decrement()
            CounterContract.Event.ResetClicked -> reset()
        }
    }

    private fun increment() {
        _state.update { it.copy(count = it.count + 1) }

        // Example: emit a one-off effect
        val newCount = _state.value.count
        if (newCount == 10) {
            viewModelScope.launch {
                _effect.send(CounterContract.Effect.ShowMessage("🎉 Hit 10!"))
            }
        }
    }

    private fun decrement() {
        _state.update { it.copy(count = (it.count - 1).coerceAtLeast(0)) }
    }

    private fun reset() {
        _state.update { it.copy(count = 0) }
        viewModelScope.launch {
            _effect.send(CounterContract.Effect.ShowMessage("Reset"))
        }
    }
}