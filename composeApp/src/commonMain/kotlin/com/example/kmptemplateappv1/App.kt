package com.example.kmptemplateappv1

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmptemplateappv1.navigation.NavigationRoot
import com.example.kmptemplateappv1.navigation.Route
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kmptemplateappv1.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.kmptemplateappv1.networking.createPlatformHttpClient
import com.example.kmptemplateappv1.networking.SecApi
import com.example.kmptemplateappv1.screens.TodoListScreen
import com.example.kmptemplateappv1.theme.AppTheme
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

//import savedToken


data class BottomNavItem(
    val title: String,
    val route: NavKey
)
@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>
) {


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
            route = Route.ToProfile
        )
    )

    AppTheme {

        Scaffold(
            bottomBar = {
                NavigationBar {
                    item.forEach { navItem ->
                        NavigationBarItem(
                            icon = { Text( navItem.title) }, // simple placeholder icon
                            selected = false,
                            onClick = {
                                backStack.add(navItem.route)
                            }
                        )
                    }
                }
            }
        ){
            innerPadding ->
            NavigationRoot(
                modifier = Modifier
                    .padding(innerPadding)
                    .safeContentPadding(),
                backStack = backStack
            )
        }
    }
}