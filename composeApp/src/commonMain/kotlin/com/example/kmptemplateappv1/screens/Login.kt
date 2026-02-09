package com.example.kmptemplateappv1.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kmptemplateappv1.Greeting
import com.example.kmptemplateappv1.networking.SecApi
import com.example.kmptemplateappv1.networking.createPlatformHttpClient
import com.example.kmptemplateappv1.viewmodels.LoginViewModel
import kmptemplateappv1.composeapp.generated.resources.Res
import kmptemplateappv1.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import io.ktor.client.HttpClient

@Composable
fun LoginScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login screen")
        Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
    var showContent by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    // Create clients once per composition

    /*
        val client: HttpClient = remember {  createPlatformHttpClient() }
        val api = remember { SecApi(client, "https://sec.sdlab.dk") }

        val savedToken by prefs
            .data
            .map {
                val tokenKey = stringPreferencesKey("tokenKey")
                it[tokenKey] ?: "No token saved"
            }
            .collectAsState("No token saved")
        LazyColumn(


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
                        if (res?.access_token != null) {
                            prefs.edit { dataStore ->
                                val counterKey = stringPreferencesKey("tokenKey")
                                dataStore[counterKey] = res.access_token
                            }
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
                    status = savedToken // already a String
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
        */

}