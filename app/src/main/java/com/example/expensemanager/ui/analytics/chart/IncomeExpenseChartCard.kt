package com.example.expensemanager.ui.analytics.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expensemanager.utils.IncomeExpenseChartPoint
import com.example.expensemanager.utils.formatCompactCurrency

@Composable
fun IncomeExpenseChartCard(
    points: List<IncomeExpenseChartPoint>
) {
    val totalIncome = points.sumOf { it.income }
    val totalExpense = points.sumOf { it.expense }
    val net = totalIncome - totalExpense

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Income vs Expense Chart",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Daily income and expense over the last 14 days",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChartLegendItem(
                    label = "Income",
                    color = MaterialTheme.colorScheme.primary
                )

                ChartLegendItem(
                    label = "Expense",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (points.all { it.income == 0.0 && it.expense == 0.0 }) {
                Text(
                    text = "No income or expense data for this period.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                SimpleMultiLineChart(
                    points = points
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = points.firstOrNull()?.label ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = points.getOrNull(points.size / 2)?.label ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = points.lastOrNull()?.label ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Income: ${formatCompactCurrency(totalIncome)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Expense: ${formatCompactCurrency(totalExpense)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Net: ${formatCompactCurrency(net)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (net >= 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }
}

@Composable
private fun ChartLegendItem(
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.size(6.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}