package com.example.kmptemplateappv1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmptemplateappv1.screens.Control
import com.example.kmptemplateappv1.screens.GroupList
import com.example.kmptemplateappv1.screens.LoginScreen
import com.example.kmptemplateappv1.screens.Profile
import com.example.kmptemplateappv1.screens.TodoDetailScreen
import com.example.kmptemplateappv1.screens.TodoListScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>
) {

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()

        ),
        entryProvider = { key ->
            when (key) {
                is Route.TodoList -> {
                    NavEntry(key) {
                        TodoListScreen(
                            onTodoClick = {
                                backStack.add(Route.TodoDetails(it))
                            }
                        )
                    }
                }

                is Route.TodoDetails -> {
                    NavEntry(key) {
                        TodoDetailScreen(
                            todo = key.todo,
                            onBackClick = {
                                backStack.add(Route.TodoList)
                            }

                        )
                    }
                }

                is Route.TodoDetails -> {
                    NavEntry(key) {
                        LoginScreen(

                        )
                    }
                }

                is Route.ToLogin -> {
                    NavEntry(key) {
                        LoginScreen(

                        )
                    }
                }

                is Route.ToControl -> {
                    NavEntry(key) {
                        Control()
                    }
                }

                is Route.ToGroupList -> {
                    NavEntry(key) {
                        GroupList()
                    }
                }

                is Route.ToProfile -> {
                    NavEntry(key) {
                        Profile()
                    }
                }


                else -> error("Unknown route: $key")
            }
        }
    )
}