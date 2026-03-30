package com.example.kmptemplateappv1

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmptemplateappv1.data.di.aacModule
import com.example.kmptemplateappv1.data.networking.AacClient


import com.example.kmptemplateappv1.data.networking.ApiService
import com.example.kmptemplateappv1.data.networking.ApiResult
import com.example.kmptemplateappv1.data.networking.createPlatformHttpClient
import com.example.kmptemplateappv1.navigation.NavigationRoot
import com.example.kmptemplateappv1.navigation.Route

import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.map
import com.example.kmptemplateappv1.theme.AppTheme

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.core.component.getScopeName
import util.onError
import util.onSuccess



data class BottomNavItem(
    val title: String,
    val route: NavKey
)

@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>
) {
    KoinContext {
        val client = koinInject<AacClient>()


        var uncensoredText by remember {
            mutableStateOf("")
        }
        var isLoading by remember {
            mutableStateOf(false)
        }
        var errorMessage by remember {
            mutableStateOf("")
        }


        // Create the multiplatform API service using Ktor

        val httpClient = createPlatformHttpClient()




        // State for API response

        // Example: Fetch version on launch


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
                        subclass(Route.ToGroupList::class , Route.ToGroupList.serializer())

                    }
                }
            },
            Route.ToGroupList(1)  // Landing page - change this to set different starting screen
        )

        // Navigate to login if no token
        LaunchedEffect(savedToken) {
            if (savedToken == "No token saved") {
                backStack.add(Route.ToLogin)
            }
            backStack.add(Route.ToGroupList(1))
        }

        val item = listOf(
            BottomNavItem(
                title = "Groups",
                route = Route.ToGroupList(1)
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
                                icon = { Text(navItem.title) },
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
