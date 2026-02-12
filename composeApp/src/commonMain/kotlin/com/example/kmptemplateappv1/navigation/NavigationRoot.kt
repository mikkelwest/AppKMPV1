package com.example.kmptemplateappv1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.kmptemplateappv1.presentation.control.Control
import com.example.kmptemplateappv1.screens.GroupList
import com.example.kmptemplateappv1.presentation.login.Login
import com.example.kmptemplateappv1.presentation.login.LoginViewModel
import com.example.kmptemplateappv1.screens.Profile
import com.example.kmptemplateappv1.screens.TodoDetailScreen
import com.example.kmptemplateappv1.screens.TodoListScreen
import com.example.kmptemplateappv1.presentation.counter.CounterScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>,
    prefs: DataStore<Preferences>
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
                is Route.TodoList ->
                    NavEntry(key) {
                        TodoListScreen(
                            onTodoClick = { backStack.add(Route.TodoDetails(it)) }
                        )
                    }

                is Route.TodoDetails ->
                    NavEntry(key) {
                        TodoDetailScreen(
                            todo = key.todo,
                            onBackClick = { backStack.last() }
                        )
                    }

                is Route.ToControl ->
                    NavEntry(key) {
                        Control()
                    }

                is Route.ToGroupList ->
                    NavEntry(key) {
                        GroupList()
                    }

                is Route.ToProfile ->
                    NavEntry(key) {
                        Profile()
                    }

                is Route.ToCounter
                    ->
                    NavEntry(key) {
                        CounterScreen()
                    }

                is Route.ToLogin ->
                    NavEntry(key) {
                        val vm = LoginViewModel()
                        Login(
                            prefs = prefs,
                            viewModel = vm,
                            onNavigateHome = {
                                backStack.add(Route.ToProfile)
                            },
                            onNavigateRegister = {
                                println("Navigate to Register screen")
                            }
                        )
                    }

                else -> error("Unknown route: $key")
            }
        }
    )
}