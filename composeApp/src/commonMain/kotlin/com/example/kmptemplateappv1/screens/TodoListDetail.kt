package com.example.kmptemplateappv1.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kmptemplateappv1.navigation.Route
import com.example.kmptemplateappv1.viewmodels.TodoDetailViewModel

@Composable
fun TodoDetailScreen(
    onBackClick: () -> Unit,
    todo: String,
    viewModel: TodoDetailViewModel = viewModel() {
        TodoDetailViewModel(todo)
    },
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(1.dp)
    ) {
        Text(
            text = "Back",
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = todo,
                fontSize = 24.sp
            )
        }
    }
}
