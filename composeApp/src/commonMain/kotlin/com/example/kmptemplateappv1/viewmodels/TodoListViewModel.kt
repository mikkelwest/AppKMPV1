package com.example.kmptemplateappv1.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoListViewModel : ViewModel() {
    private val _todos = MutableStateFlow(
        (1..100).map { "Todo Item #$it" }
    )
    val todos = _todos.asStateFlow()

}