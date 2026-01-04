package com.example.kmptemplateappv1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform