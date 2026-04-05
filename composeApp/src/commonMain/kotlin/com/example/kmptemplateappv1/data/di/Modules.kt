package com.example.kmptemplateappv1.data.di


import com.example.kmptemplateappv1.data.dependencies.MyRepository
import com.example.kmptemplateappv1.data.dependencies.MyRepositoryImpl
import com.example.kmptemplateappv1.presentation.profile.CounterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

//expect val platformModule: Module

val sharedModule = module {
    singleOf(::MyRepositoryImpl).bind<MyRepository>()
    factory { CounterViewModel() }
}