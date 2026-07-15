package com.example.expensemanager.ui.analytics.chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.expensemanager.utils.ChartPoint
import com.example.expensemanager.utils.formatCompactNumber

@Composable
fun SimpleLineChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.outline
    val gridColor = MaterialTheme.colorScheme.surfaceVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(
        modifier = modifier
    ) {
        if (points.isEmpty()) return@Canvas

        val leftPadding = 58.dp.toPx()
        val rightPadding = 12.dp.toPx()
        val topPadding = 18.dp.toPx()
        val bottomPadding = 26.dp.toPx()

        val chartWidth = size.width - leftPadding - rightPadding
        val chartHeight = size.height - topPadding - bottomPadding

        val rawMax = points.maxOfOrNull { it.value } ?: 0.0
        val rawMin = points.minOfOrNull { it.value } ?: 0.0

        val maxValue = rawMax.coerceAtLeast(0.0)
        val minValue = rawMin.coerceAtMost(0.0)

        val range = (maxValue - minValue).coerceAtLeast(1.0)

        val leftX = leftPadding
        val rightX = leftPadding + chartWidth
        val bottomY = topPadding + chartHeight

        val textPaint = Paint().apply {
            isAntiAlias = true
            color = labelColor.toArgb()
            textSize = 11.dp.toPx()
            textAlign = Paint.Align.RIGHT
        }

        val labelX = leftX - 8.dp.toPx()

        fun valueToY(value: Double): Float {
            return bottomY - (((value - minValue) / range).toFloat() * chartHeight)
        }

        val middleValue = (maxValue + minValue) / 2.0

        val yLabels = listOf(
            maxValue to valueToY(maxValue),
            middleValue to valueToY(middleValue),
            minValue to valueToY(minValue)
        )

        yLabels.forEach { (value, y) ->
            drawLine(
                color = gridColor,
                start = Offset(leftX, y),
                end = Offset(rightX, y),
                strokeWidth = 1.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.drawText(
                formatCompactNumber(value),
                labelX,
                y + 4.dp.toPx(),
                textPaint
            )
        }

        if (minValue < 0.0 && maxValue > 0.0) {
            val zeroY = valueToY(0.0)

            drawLine(
                color = axisColor,
                start = Offset(leftX, zeroY),
                end = Offset(rightX, zeroY),
                strokeWidth = 1.6.dp.toPx()
            )
        }

        drawLine(
            color = axisColor,
            start = Offset(leftX, topPadding),
            end = Offset(leftX, bottomY),
            strokeWidth = 1.4.dp.toPx()
        )

        drawLine(
            color = axisColor,
            start = Offset(leftX, bottomY),
            end = Offset(rightX, bottomY),
            strokeWidth = 1.4.dp.toPx()
        )

        val chartPoints = points.mapIndexed { index, point ->
            val x = if (points.size == 1) {
                leftX + chartWidth / 2f
            } else {
                leftX + chartWidth * index / (points.size - 1)
            }

            val y = valueToY(point.value)

            Offset(x, y)
        }

        for (index in 0 until chartPoints.lastIndex) {
            drawLine(
                color = lineColor,
                start = chartPoints[index],
                end = chartPoints[index + 1],
                strokeWidth = 3.dp.toPx()
            )
        }

        chartPoints.forEach { point ->
            drawCircle(
                color = pointColor,
                radius = 4.dp.toPx(),
                center = point
            )
        }
    }
}