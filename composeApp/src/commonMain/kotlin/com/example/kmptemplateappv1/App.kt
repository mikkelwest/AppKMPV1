package com.example.kmptemplateappv1


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmptemplateappv1.data.networking.ApiResult
import com.example.kmptemplateappv1.data.networking.ApiService
import com.example.kmptemplateappv1.data.networking.PostAssetIntValueRequest
import com.example.kmptemplateappv1.data.networking.createPlatformHttpClient
import com.example.kmptemplateappv1.navigation.NavigationRoot
import com.example.kmptemplateappv1.navigation.Route
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.map

import com.example.kmptemplateappv1.theme.AppTheme
import httpClient
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.KoinContext


data class BottomNavItem(
    val title: String,
    val route: NavKey
)
@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>
) {
    val httpClient = remember { createPlatformHttpClient() }
    val apiService = remember { ApiService(httpClient, "https://poset-api.fly.dev/api/v1") }

    // Call API methods (all are suspend functions)
        LaunchedEffect(Unit) {
            // Get version
            when (val result = apiService.getVersion()) {
                is ApiResult.Success -> println("Version: ${result.data}")
                is ApiResult.Error -> println("Error: ${result.message}")
            }

            // Get asset config
            when (val result = apiService.getAssetConfigIntValue(assetId = 123)) {
                is ApiResult.Success -> println("Success!")
                is ApiResult.Error -> println("Error: ${result.message}")
            }

            // Post new asset config
            val request = PostAssetIntValueRequest(assetId = 1, key = "myKey", value = 42)
            apiService.postAssetConfigIntValue(request)
        }


    KoinContext {
        // Create the multiplatform API service using Ktor
        val httpClient = remember { createPlatformHttpClient() }
        val apiService = remember { ApiService(httpClient, "https://poset-api.fly.dev/api/v1") }

        // Example: Fetch version on launch
        LaunchedEffect(Unit) {
            when (val result = apiService.getVersion()) {
                is ApiResult.Success -> println("API Version: ${result.data}")
                is ApiResult.Error -> println("API Error: ${result.message}")
            }
        }

        val savedToken by prefs
            .data
            .map {
                val tokenKey = stringPreferencesKey("auth_token")
                it[tokenKey] ?: "No token saved"
            }
            .collectAsState("No token saved")

        val backStack: NavBackStack<NavKey> = rememberNavBackStack(
            configuration = SavedStateConfiguration {
                serializersModule = SerializersModule {
                    polymorphic(NavKey::class) {
                        subclass(Route.TodoList::class, Route.TodoList.serializer())
                        subclass(Route.TodoDetails::class, Route.TodoDetails.serializer())
                        subclass(Route.ToLogin::class, Route.ToLogin.serializer())
                    }
                }
            },
            Route.TodoList
        )

        // Navigate to login if no token
        LaunchedEffect(savedToken) {
            if (savedToken == "No token saved") {
                backStack.add(Route.ToLogin)
            } else {
                // Optionally, you could validate the token here before navigating to the main content
                backStack.add(Route.TodoList)
            }
        }

        // Rest of your code...


        val item = listOf(
            BottomNavItem(
                title = "Groups",
                route = Route.ToGroupList

            ),
            BottomNavItem(
                title = "Control",
                route = Route.ToControl
            ),
            BottomNavItem(
                title = "Profile",
                route = Route.ToCounter
            )
        )

        AppTheme {

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        item.forEach { navItem ->
                            NavigationBarItem(
                                icon = { Text(navItem.title) }, // simple placeholder icon
                                selected = false,
                                onClick = {
                                    backStack.add(navItem.route)
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavigationRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .safeContentPadding(),
                    backStack = backStack,
                    prefs = prefs
                )
            }
        }
    }
}