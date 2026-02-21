package com.example.kmptemplateappv1.data.di


import com.example.kmptemplateappv1.data.networking.AacClient
import com.example.kmptemplateappv1.data.networking.createPlatformHttpClient
import com.example.kmptemplateappv1.presentation.group.GroupViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val aacModule = module {
    single { createPlatformHttpClient() }
    single { AacClient(get()) }
    viewModel { GroupViewModel(get()) }
}