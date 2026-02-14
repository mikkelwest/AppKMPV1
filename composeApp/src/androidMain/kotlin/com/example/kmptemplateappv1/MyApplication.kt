package com.example.kmptemplateappv1

import com.example.kmptemplateappv1.data.di.initKoin

class MyApplication: android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}