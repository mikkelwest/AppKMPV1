package com.example.kmptemplateappv1.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.kmptemplateappv1.Greeting
import kmptemplateappv1.composeapp.generated.resources.Res
import kmptemplateappv1.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun Login(
    prefs: DataStore<Preferences>,
    viewModel: LoginViewModel,
    onNavigateHome: () -> Unit = {},
    onNavigateRegister: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    // Observe one-off effects (navigation, toast, etc.)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginContract.Effect.NavigateToHome -> onNavigateHome()
                LoginContract.Effect.NavigateToRegister -> onNavigateRegister()
                is LoginContract.Effect.ShowError -> { /* could show snackbar */ }
            }
        }
    }

    // For demo: still show the greeting block when toggled
    var showContent by remember { mutableStateOf(false) }
    // api.login("mikkelwestnielsen@gmail.com", "33129119")
    val savedToken by prefs
        .data
        .map {
            val tokenKey = stringPreferencesKey("auth_token")
            it[tokenKey] ?: "No token saved"
        }
        .collectAsState("No token saved")

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(LoginContract.Event.OnEmailChanged(it)) {} },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(LoginContract.Event.OnPasswordChanged(it)) {} },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage.toString(),
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                viewModel.onEvent(LoginContract.Event.OnLoginClicked) { token ->
                    scope.launch {
                        prefs.edit { dataStore ->
                            val key = stringPreferencesKey("auth_token")
                            dataStore[key] = token
                        }
                    }
                }
            },
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Login")
            }
        }

        Button(onClick = { showContent = !showContent }) {
            Text("Toggle greeting")
        }

        Button(onClick = { /* Show stored token for debug */ }) {
            Text(savedToken)
        }

        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}