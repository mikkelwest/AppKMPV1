package com.example.kmptemplateappv1.presentation.counter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

/**
 * If you have lifecycle-compose dependency, prefer:
 * collectAsStateWithLifecycle()
 *
 * This is a simple fallback to keep the example self-contained.
 */
@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycleCompat(): State<T> = collectAsState()