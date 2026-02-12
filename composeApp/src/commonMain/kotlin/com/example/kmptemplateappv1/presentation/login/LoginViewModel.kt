package com.example.kmptemplateappv1.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmptemplateappv1.data.networking.SecApi
import com.example.kmptemplateappv1.data.networking.createPlatformHttpClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val client = createPlatformHttpClient()
    private val api = SecApi(client, "https://sec.sdlab.dk")

    private val _state = MutableStateFlow(LoginContract.State())
    val state: StateFlow<LoginContract.State> = _state.asStateFlow()

    private val _effect = Channel<LoginContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginContract.Event, onTokenReceived: suspend (String) -> Unit) {
        when (event) {
            is LoginContract.Event.OnEmailChanged -> {
                _state.update { it.copy(email = event.email, errorMessage = null) }
            }
            is LoginContract.Event.OnPasswordChanged -> {
                _state.update { it.copy(password = event.password, errorMessage = null) }
            }
            is LoginContract.Event.OnLoginClicked -> {
                login(onTokenReceived)
            }
            is LoginContract.Event.OnRegisterClicked -> {
                viewModelScope.launch {
                    _effect.send(LoginContract.Effect.NavigateToRegister)
                }
            }
        }
    }

    private fun login(onTokenReceived: suspend (String) -> Unit) {
        val current = _state.value

        if (current.email.isBlank()) {
            _state.update { it.copy(errorMessage = "Email is required") }
            return
        }
        if (current.password.isBlank()) {
            _state.update { it.copy(errorMessage = "Password is required") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val res = api.login(current.email, current.password)
                val access = res?.access_token
                if (!access.isNullOrBlank()) {
                    _state.update { it.copy(isLoading = false) }
                    onTokenReceived(access)
                    _effect.send(LoginContract.Effect.NavigateToHome)
                } else {
                    _state.update { it.copy(isLoading = false, errorMessage = "Login failed") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.message ?: "Login error") }
                _effect.send(LoginContract.Effect.ShowError(e.message ?: "Login error"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}