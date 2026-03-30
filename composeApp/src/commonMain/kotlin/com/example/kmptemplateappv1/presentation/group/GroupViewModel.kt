package com.example.kmptemplateappv1.presentation.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmptemplateappv1.data.networking.AacClient
import com.example.kmptemplateappv1.data.networking.ApiClient
import com.example.kmptemplateappv1.data.networking.Response.UnitIntHistotoryResponse
import com.example.kmptemplateappv1.data.networking.Response.UnitResponse
import com.example.kmptemplateappv1.domain.model.Group
import com.example.kmptemplateappv1.domain.model.UnitConfig
import com.example.kmptemplateappv1.domain.model.toGroup
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import util.onError
import util.onSuccess

class GroupViewModel(
    private val aacClient: AacClient,
    private val apiClient: ApiClient
) : ViewModel() {

    private val selectedGroup = 1

    private val _state = MutableStateFlow(GroupContract.State())
    val state: StateFlow<GroupContract.State> = _state.asStateFlow()

    private val _groups: MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups = _groups.asStateFlow()

    private val _units: MutableStateFlow<List<UnitResponse>> = MutableStateFlow(emptyList())
    val units = _units.asStateFlow()


    private val _groupId: MutableStateFlow<Int> = MutableStateFlow(0)
    val groupId = _groupId.asStateFlow()

    private val _effect = Channel<GroupContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(100)
            loadGroups(state.value.selectedGroup)
        }
    }

    suspend fun loadGroups(groupNumber: Int) {
        try {
            val result = aacClient.groupSlim(groupNumber)
            result.onSuccess { apiResult ->
                // Use the toGroup() extension to map response to domain model
                _groups.value += apiResult.subGroups.map { it.toGroup() }

                println("API Success: ${apiResult.groupId}")
            }.onError { error ->
                println("API Error: $error")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    suspend fun saveUnits(unitNumber: Int, unit: UnitConfig) {
        try {
            val result = apiClient.saveUnitConfig( unitNumber, unit  )
            result.onSuccess { apiResult ->
                // Use the toGroup() extension to map response to domain model
                println("API saveUnits Success: ${apiResult}")
                //_units.value = apiResult

            }.onError { error ->
                println("API Error: $error")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    suspend fun loadUnit(unitNumber: Int): List<UnitResponse>? {
        try {
            val result = apiClient.unitConfig(unitNumber)
            result.onSuccess { apiResult ->
                // Use the toGroup() extension to map response to domain model
                //_groups.value = apiResult.value { it.toGroup() }
                println("API Success: ${apiResult[0].value}")
                return apiResult
            }.onError { error ->
                println("API Error: $error")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
        return null
    }

    suspend fun loadUnitHistory(unitNumber: String): List<UnitIntHistotoryResponse>? {
        try {
            val result = apiClient.unitIntHistory(unitNumber)
            result.onSuccess { apiResult ->
                // Use the toGroup() extension to map response to domain model
                //_groups.value = apiResult.value { it.toGroup() }
                println("API Success: ${apiResult[0].value}")
                return apiResult
            }.onError { error ->
                println("API Error: $error")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
        return null
    }


    fun onEvent(event: GroupContract.Event): Unit {
            when (event) {
                is GroupContract.Event.OnGroupSelected -> {
                    println("Event Selected group: ${event.groupId}")

                    viewModelScope.launch {
                        //GroupContract.Effect.NavigateToGroup (event.groupId)
                        println("Event Selected group: ${event.groupId}")
                        _effect.send(GroupContract.Effect.NavigateToGroup(event.groupId))
                    }
                }

                is GroupContract.Event.OnBackClicked -> {
                    viewModelScope.launch {
                        println("Event Back:")
                        _effect.send(GroupContract.Effect.NavigateBack)
                    }
                }

                is GroupContract.Event.OnUpdateUnits -> {
                    viewModelScope.launch {
                        println("Event updateUnits:")
                        saveUnits(event.unitId, event.unitConfig )
                    }
                }
            }
    }
}







