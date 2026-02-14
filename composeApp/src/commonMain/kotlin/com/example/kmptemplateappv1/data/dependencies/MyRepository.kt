package com.example.kmptemplateappv1.data.dependencies

interface MyRepository {
    fun helloWorld(): String
}

class MyRepositoryImpl(

) : MyRepository {
    override fun helloWorld(): String {
        return "Hello World!!!!!"
    }
}