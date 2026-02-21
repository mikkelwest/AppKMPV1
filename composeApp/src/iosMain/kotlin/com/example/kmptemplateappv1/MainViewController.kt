package com.example.kmptemplateappv1

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.example.kmptemplateappv1.data.di.initKoin
import com.example.kmptemplateappv1.data.networking.AacClient
import com.example.kmptemplateappv1.data.networking.createHttpClient
import io.ktor.client.engine.darwin.Darwin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App(
    prefs = remember {
        createDataStore()
    },
    client = remember {
        AacClient(createHttpClient(Darwin.create()))
    }
) }