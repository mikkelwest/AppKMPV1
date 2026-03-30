package com.example.kmptemplateappv1

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.example.kmptemplateappv1.data.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App(
        prefs = remember {
            createDataStore()
        }
    )
}
