package com.example.kmptemplateappv1.presentation.group

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.chaintech.cmpcharts.axis.AxisData
import network.chaintech.cmpcharts.common.model.Point
import network.chaintech.cmpcharts.ui.linechart.LineChart
import network.chaintech.cmpcharts.ui.linechart.model.Line
import network.chaintech.cmpcharts.ui.linechart.model.LineChartData
import network.chaintech.cmpcharts.ui.linechart.model.LinePlotData
import network.chaintech.cmpcharts.ui.linechart.model.LineStyle

// Define your colors
val purple_dark = Color(0xFF6200EE)
val green_dark = Color(0xFF03DAC5)
val magenta_dark = Color(0xFFBB86FC)
val white_color = Color.White
val gray_light = Color.LightGray
val font_color = Color.Black

// Helper extension function
fun Float.formatToSinglePrecision(): String {
    val rounded = (this * 10).toInt() / 10.0
    return rounded.toString()
}

@Composable
fun LineChartExample(pointsData: List<Point>) {
    val steps = 5

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .topPadding(10.dp)
        .axisLabelColor(font_color)
        .axisLineColor(font_color)
        .axisLabelFontSize(8.sp)
        .steps(pointsData.size - 1)
        .labelData { i -> pointsData.getOrNull(i)?.x?.toInt()?.toString() ?: "" }
        .labelAndAxisLinePadding(10.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .axisStepSize(20.dp)
        .steps(steps)
        .axisLabelColor(font_color)
        .axisLineColor(font_color)
        .labelAndAxisLinePadding(10.dp)
        .axisLabelFontSize(10.sp)
        .labelData { i ->
            val yMin = pointsData.minOfOrNull { it.y } ?: 0f
            val yMax = pointsData.maxOfOrNull { it.y } ?: 1f
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }
        .build()

    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(color = purple_dark)
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        lineChartData = data
    )
}
