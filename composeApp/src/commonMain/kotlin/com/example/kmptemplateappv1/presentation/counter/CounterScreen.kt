package com.example.kmptemplateappv1.presentation.counter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kmptemplateappv1.data.dependencies.MyRepository
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun CounterScreen(
    vm: CounterViewModel = viewModel()
) {
    val state = vm.state.collectAsStateWithLifecycleCompat()
    val snackbarHostState = remember { SnackbarHostState() }
    val aa = koinInject<MyRepository>()
    val aaa = aa.helloWorld()

    // Collect one-off effects
    LaunchedEffect(Unit) {
        vm.effect.collectLatest { effect ->
            when (effect) {
                is CounterContract.Effect.ShowMessage ->
                    snackbarHostState.showSnackbar(effect.text)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SnackbarHost(hostState = snackbarHostState)

        Text(text = "Count: ${state.value.count}")
        Spacer(Modifier.height(16.dp))

        Button(onClick = { vm.onEvent(CounterContract.Event.IncrementClicked) }) {
            Text("Increment")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { vm.onEvent(CounterContract.Event.DecrementClicked) }) {
            Text("Decrement")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { vm.onEvent(CounterContract.Event.ResetClicked) }) {
            Text("Reset")
        }
        Spacer(Modifier.height(8.dp))
        Text(text = "di: ${aa.helloWorld()}")
    }
}