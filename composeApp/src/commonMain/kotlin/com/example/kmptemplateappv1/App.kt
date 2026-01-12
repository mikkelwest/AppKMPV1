package com.example.kmptemplateappv1

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmptemplateappv1.composeapp.generated.resources.Res
import kmptemplateappv1.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import networking.createPlatformHttpClient
import networking.SecApi
import networking.tokenStore

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var status by remember { mutableStateOf<String?>(null) }
        val coroutineScope = rememberCoroutineScope()
        // Create clients once per composition
        val client = remember { createPlatformHttpClient() }
        val api = remember { SecApi(client, "https://sec.sdlab.dk") }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            Button(onClick = {
                status = "Logging in..."
                coroutineScope.launch {
                    try {
                        val res = api.login("mikkelwestnielsen@gmail.com", "33129119")
                        status = if (res != null && !res.access_token.isNullOrBlank()) {
                            "Login success"
                        } else {
                            "Login failed or no tokens returned"
                        }
                    } catch (e: Exception) {
                        status = "Login error: ${'$'}{e.message}"
                    }
                }
            }) {
                Text("Login!")
            }
            Button(onClick = {
                coroutineScope.launch {
                    val token = tokenStore.getAccessToken()
                    status = token ?: "No token saved"
                }
            }) {
                Text("Show token")
            }

            if (status != null) {
                Text(status!!)
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
}