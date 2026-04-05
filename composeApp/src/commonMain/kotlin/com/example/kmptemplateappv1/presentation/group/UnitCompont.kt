package com.example.kmptemplateappv1.presentation.group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kmptemplateappv1.data.networking.Response.UnitIntHistotoryResponse
import com.example.kmptemplateappv1.domain.model.UnitConfig

@Composable
fun UnitComponent(
    unitId: Int,
    viewModel: GroupViewModel,
) {
    var name by remember { mutableStateOf("Loading...") }
    var value by remember { mutableStateOf("Loading...") }
    var isEditable by remember { mutableStateOf(false) }
    var isEditableActive by remember { mutableStateOf(false) }
    var thisUnitHistory by remember { mutableStateOf<List<UnitIntHistotoryResponse>?>(null) }
    var unitConfig : UnitConfig by remember { mutableStateOf(UnitConfig()) }
    var tempValue by remember { mutableStateOf("") }

    LaunchedEffect(unitId,unitConfig ) {
        val unitResponse = viewModel.loadUnit(unitId)


        unitConfig = UnitConfig(
            assetsGuid = unitResponse?.firstOrNull()?.assetsGuid ?: "",
            name = unitResponse?.firstOrNull()?.name ?: "",
            value = unitResponse?.firstOrNull()?.value ?: 0,
            assetkey = unitResponse?.firstOrNull()?.assetkey ?: ""
        )

        val unitAssetsGuid = unitResponse?.firstOrNull()?.assetsGuid ?: ""

        if (unitAssetsGuid != "") {
            thisUnitHistory = viewModel.loadUnitHistory(unitAssetsGuid) // Remove 'val'
            if (thisUnitHistory?.firstOrNull()?.value == null) {
                println("No historical data available for unit with assetsGuid: $unitAssetsGuid")
            } else {
                println("Historical data for unit with assetsGuid: $unitAssetsGuid - ${thisUnitHistory?.firstOrNull()?.value}")
            }
        }

        name = unitResponse?.firstOrNull()?.name ?: "Unknown Unit"
        value = unitResponse?.firstOrNull()?.value?.toString() ?: "Unknown Value"
        isEditable = unitResponse?.firstOrNull()?.isEditable ?: false
        tempValue = value
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Column 1: Main content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            if (isEditable && isEditableActive) {
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue -> value = newValue },
                    label = { Text(name + " : " + unitId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                )
                } else {
                    Text(
                        text = name + " : " + value + " : " + unitId,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)

                    )
                }
                if (thisUnitHistory?.firstOrNull()?.value != null) {
                    var listOfPoint =
                        thisUnitHistory?.map { Point(it.id.toFloat(), it.value?.toFloat() ?: 0f) }
                            ?: emptyList()
                    LineChartExample(pointsData = listOfPoint)
                }
            }

        // Column 2: Edit button
        Column(
            modifier = Modifier
                .padding(2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (
                isEditable
            ){
                Column {
                    Text(
                        text = "Edit",
                        modifier = Modifier
                            .padding(2.dp)
                            .clickable { isEditableActive = !isEditableActive }
                    )
                    if(isEditableActive){
                        Text(
                            text = "Save",
                            modifier = Modifier
                                .padding(2.dp)
                                .clickable {
                                    print("Start Event Saving value: $value for unitId: $unitId with assetsGuid: ${unitConfig.assetsGuid}")
                                    isEditableActive = !isEditableActive
                                    unitConfig.value = value.toIntOrNull() ?: 0
                                    tempValue = value
                                    viewModel.onEvent(GroupContract.Event.OnUpdateUnits(unitId, unitConfig))
                                }
                        )
                        Text(
                            text = "Cancel",
                            modifier = Modifier
                                .padding(2.dp)
                                .clickable {
                                    isEditableActive = !isEditableActive
                                    value = tempValue
                                }
                        )
                    }
                }
            }
        }
    }

}
