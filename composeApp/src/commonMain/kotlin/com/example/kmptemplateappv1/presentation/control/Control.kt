package com.example.kmptemplateappv1.presentation.control

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.kmptemplateappv1.data.dependencies.MyRepository
import com.example.kmptemplateappv1.data.networking.AacClient
import org.koin.compose.koinInject
import org.koin.core.component.getScopeName
import util.onError
import util.onSuccess

@Composable
fun Control(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val client = koinInject<AacClient>()
    val aaa = koinInject<MyRepository>()
    var response by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val groups = client.groupSlim(1)
            val aa = groups.onSuccess { result ->
                println("API Success: ${result.groupId}")
            }.onError { error ->
                println("API Error: $error")
            }
            val bb = aa.getScopeName()

        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(1.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Response: ${response.toString()}")
        Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
}