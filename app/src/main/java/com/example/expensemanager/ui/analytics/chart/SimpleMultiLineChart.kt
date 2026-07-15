package com.example.expensemanager.ui.analytics.chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.expensemanager.utils.IncomeExpenseChartPoint
import com.example.expensemanager.utils.formatCompactNumber

@Composable
fun SimpleMultiLineChart(
    points: List<IncomeExpenseChartPoint>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(210.dp)
) {
    val incomeColor = MaterialTheme.colorScheme.primary
    val expenseColor = MaterialTheme.colorScheme.error
    val axisColor = MaterialTheme.colorScheme.outline
    val gridColor = MaterialTheme.colorScheme.surfaceVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(
        modifier = modifier
    ) {
        if (points.isEmpty()) return@Canvas

        val leftPadding = 54.dp.toPx()
        val rightPadding = 12.dp.toPx()
        val topPadding = 18.dp.toPx()
        val bottomPadding = 26.dp.toPx()

        val chartWidth = size.width - leftPadding - rightPadding
        val chartHeight = size.height - topPadding - bottomPadding

        val maxIncome = points.maxOfOrNull { it.income } ?: 0.0
        val maxExpense = points.maxOfOrNull { it.expense } ?: 0.0
        val maxValue = maxOf(maxIncome, maxExpense).coerceAtLeast(1.0)

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

        val yLabels = listOf(
            maxValue to topPadding,
            maxValue / 2.0 to topPadding + chartHeight / 2f,
            0.0 to bottomY
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

        fun buildOffsets(valueSelector: (IncomeExpenseChartPoint) -> Double): List<Offset> {
            return points.mapIndexed { index, point ->
                val x = if (points.size == 1) {
                    leftX + chartWidth / 2f
                } else {
                    leftX + chartWidth * index / (points.size - 1)
                }

                val value = valueSelector(point)
                val y = bottomY - ((value / maxValue).toFloat() * chartHeight)

                Offset(x, y)
            }
        }

        fun drawSeries(
            offsets: List<Offset>,
            color: Color
        ) {
            for (index in 0 until offsets.lastIndex) {
                drawLine(
                    color = color,
                    start = offsets[index],
                    end = offsets[index + 1],
                    strokeWidth = 3.dp.toPx()
                )
            }

            offsets.forEach { point ->
                drawCircle(
                    color = color,
                    radius = 4.dp.toPx(),
                    center = point
                )
            }
        }

        val incomeOffsets = buildOffsets { it.income }
        val expenseOffsets = buildOffsets { it.expense }

        drawSeries(
            offsets = incomeOffsets,
            color = incomeColor
        )

        drawSeries(
            offsets = expenseOffsets,
            color = expenseColor
        )
    }
}