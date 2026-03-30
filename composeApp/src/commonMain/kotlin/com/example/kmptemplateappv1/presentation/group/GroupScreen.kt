package com.example.kmptemplateappv1.presentation.group

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kmptemplateappv1.presentation.login.LoginContract
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon

import kotlin.text.toInt

@Composable
fun GroupScreen(
    viewModel: GroupViewModel = koinViewModel(),
    selectedGroup: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    prefs: DataStore<Preferences>,
    navigateToGroup: (Int) -> Unit = {},
    ) {

    val state by viewModel.state.collectAsState()
    state.selectedGroup = selectedGroup

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GroupContract.Effect.NavigateToGroup -> {
                    // Navigate to the group using your navigation system
                    println("NavigateToGroup group: ${effect.groupId}")
                    navigateToGroup( effect.groupId )
                }
                is GroupContract.Effect.NavigateBack -> {
                        println("NavigateBack")
                        onBackClick()
                }
                else -> {}
            }
        }
    }

    val groups by viewModel.groups.collectAsStateWithLifecycle()
    //val aa  = viewModel.loadUnit(3)
    val scope = rememberCoroutineScope()



    Column(
        modifier = modifier.fillMaxSize().padding(1.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp)
        ) {
            items(groups) { group ->
                Column(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = group.groupName ?: "Unnamed Group",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp)
                    )
                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                    (group.assetId).forEach { it ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            UnitComponent(it ?: 0, viewModel)
                        }
                    }
                }
            }
        }

        // Subgroup buttons at bottom
        groups.flatMap { it.subGroups }.forEach { subGroup ->
            Button(
                onClick = {
                    viewModel.onEvent(GroupContract.Event.OnGroupSelected(subGroup.groupId.toInt()))
                },
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 0.dp, start = 2.dp, end = 2.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(subGroup.groupName ?: "Unnamed Subgroup")
                }
            }
        }

        // Back button at bottom
        Button(
            onClick = { onBackClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Back")
            }
        }

    }
}

private fun GroupViewModel.onEvent(onGroupSelected: GroupContract.Event.OnGroupSelected) {}
