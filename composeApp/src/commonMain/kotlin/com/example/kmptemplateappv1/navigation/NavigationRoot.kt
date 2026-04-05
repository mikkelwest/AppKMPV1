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
import com.example.kmptemplateappv1.presentation.group.GroupScreen
import com.example.kmptemplateappv1.presentation.group.GroupViewModel
import com.example.kmptemplateappv1.presentation.login.Login
import com.example.kmptemplateappv1.presentation.login.LoginViewModel
import com.example.kmptemplateappv1.screens.Profile
import com.example.kmptemplateappv1.presentation.profile.CounterScreen
import com.example.kmptemplateappv1.presentation.profile.CounterViewModel
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.compose.koinInject

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


                is Route.ToControl ->
                    NavEntry(key) {
                        Control()
                    }

                is Route.ToGroupList ->
                    NavEntry(key) {
                        val groupViewModel: GroupViewModel = koinInject()
                        GroupScreen(
                            viewModel = groupViewModel,
                            prefs = prefs,
                            selectedGroup = key.groupNr,
                            navigateToGroup = { backStack.add(Route.ToGroupList (it) ) },
                            onBackClick = { backStack.removeLastOrNull() }
                        )
                    }



                is Route.ToProfile ->
                    NavEntry(key) {
                        Profile()
                    }

                is Route.ToCounter
                    ->
                    NavEntry(key) {
                        val counterVm: CounterViewModel = koinInject()
                        CounterScreen(vm = counterVm)
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