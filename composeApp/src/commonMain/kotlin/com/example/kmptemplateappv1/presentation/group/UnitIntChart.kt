package com.example.kmptemplateappv1.presentation.group

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

// Define your colors
val purple_dark = Color(0xFF6200EE)
val green_dark = Color(0xFF03DAC5)
val magenta_dark = Color(0xFFBB86FC)
val white_color = Color.White
val gray_light = Color.LightGray
val font_color = Color.Black

// Simple Point class to replace cmpcharts Point
data class Point(val x: Float, val y: Float)

// Helper extension function
fun Float.formatToSinglePrecision(): String {
    val rounded = (this * 10).toInt() / 10.0
    return rounded.toString()
}

@Composable
fun LineChartExample(pointsData: List<Point>) {
    if (pointsData.isEmpty()) return

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height

        val xMin = pointsData.minOfOrNull { it.x } ?: 0f
        val xMax = pointsData.maxOfOrNull { it.x } ?: 1f
        val yMin = pointsData.minOfOrNull { it.y } ?: 0f
        val yMax = pointsData.maxOfOrNull { it.y } ?: 1f

        val xRange = if (xMax - xMin == 0f) 1f else xMax - xMin
        val yRange = if (yMax - yMin == 0f) 1f else yMax - yMin

        // Draw axis lines
        drawLine(
            color = gray_light,
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = 2f
        )
        drawLine(
            color = gray_light,
            start = Offset(0f, 0f),
            end = Offset(0f, height),
            strokeWidth = 2f
        )

        // Draw the line chart
        if (pointsData.size >= 2) {
            val path = Path()
            pointsData.forEachIndexed { index, point ->
                val x = ((point.x - xMin) / xRange) * width
                val y = height - ((point.y - yMin) / yRange) * height

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = purple_dark,
                style = Stroke(width = 3f)
            )

            // Draw points
            pointsData.forEach { point ->
                val x = ((point.x - xMin) / xRange) * width
                val y = height - ((point.y - yMin) / yRange) * height
                drawCircle(
                    color = purple_dark,
                    radius = 6f,
                    center = Offset(x, y)
                )
            }
        }
    }
}
