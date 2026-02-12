package com.example.kmptemplateappv1.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


/**
 * Android-specific wrapper that adds dynamic color support on Android 12+.
 * Call this from your Android entry point instead of AppTheme if you want dynamic colors.
 */
@Composable
fun AndroidAppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val baseDark = darkScheme
    val baseLight = lightScheme

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> baseDark
        else -> baseLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

