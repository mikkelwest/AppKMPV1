package com.example.kmptemplateappv1.presentation.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmptemplateappv1.data.networking.AacClient
import com.example.kmptemplateappv1.domain.model.Group
import com.example.kmptemplateappv1.domain.model.toGroup
import com.example.kmptemplateappv1.presentation.login.LoginContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.onError
import util.onSuccess

class GroupViewModel(
    private val client: AacClient
) : ViewModel() {

    private val _groups: MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups = _groups.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            loadGroups(1)
        }
    }

    suspend fun loadGroups(groupNumber : Int) {
            try {
                val result = client.groupSlim(groupNumber)
                result.onSuccess { apiResult ->
                    // Use the toGroup() extension to map response to domain model
                    _groups.value = apiResult.subGroups.map { it.toGroup() }
                    println("API Success: ${apiResult.groupId}")
                }.onError { error ->
                    println("API Error: $error")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
    }



    suspend fun onEvent(event: GroupContract.Event) {
        when (event) {
            is GroupContract.Event.OnGroupSelected -> {
                println("Selected group: ${event.group.groupName}")
                loadGroups(event.group.groupId.toInt())
            }
        }
    }



}