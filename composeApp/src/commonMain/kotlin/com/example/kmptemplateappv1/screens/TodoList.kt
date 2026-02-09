package com.example.kmptemplateappv1.screens

import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kmptemplateappv1.viewmodels.TodoListViewModel

@Composable
fun TodoListScreen(
    viewModel: TodoListViewModel = TodoListViewModel(),
    onTodoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val todos by viewModel.todos.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp) // leave space for bottom bar
        ) {
            items(todos) { todo ->
                Text(
                    text = todo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTodoClick(todo) }
                        .padding(16.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFFEFEFEF))
                .border(1.dp, Color.Gray)
                .clickable {
                    onTodoClick("Login")
                }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "login")
        }
    }

}
