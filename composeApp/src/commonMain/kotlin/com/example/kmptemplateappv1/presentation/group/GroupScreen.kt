package com.example.kmptemplateappv1.presentation.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GroupScreen(
    viewModel: GroupViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    prefs: DataStore<Preferences>,

) {

    val groups by viewModel.groups.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize().padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 4.dp, bottom = 80.dp) // leave space for bottom bar
            ) {
                items(groups) { group ->
                    Column(
                        modifier = modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = group.groupName ?: "Unnamed Group",
                            modifier = Modifier
                                .fillMaxWidth()
                                //  .clickable { onTodoClick(todo) }
                                .padding(16.dp)
                        )
                        ( group.subGroups ) .forEach { subGroup ->
                            Button(
                                onClick = {
                                    scope.launch {
                                        viewModel.loadGroups(subGroup.groupId.toInt())
                                    }
                                    /*
                                    scope.launch {
                                        prefs.edit { dataStore ->
                                            val key = stringPreferencesKey("selected_group")
                                            dataStore[key] = subGroup.groupId.toString()
                                        }

                                    }
                                     */
                            }
                                , modifier = Modifier.padding(top = 4.dp)) {
                                Text(subGroup.groupName ?: "Unnamed Subgroup")

                            }
                        }
                    }
                }
            }
        }

    }
}