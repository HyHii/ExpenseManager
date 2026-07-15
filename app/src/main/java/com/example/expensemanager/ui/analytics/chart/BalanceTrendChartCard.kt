package com.example.expensemanager.ui.analytics.chart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expensemanager.utils.ChartPoint
import com.example.expensemanager.utils.formatCompactCurrency

@Composable
fun BalanceTrendChartCard(
    points: List<ChartPoint>
) {
    val latestBalance = points.lastOrNull()?.value ?: 0.0
    val highestBalance = points.maxByOrNull { it.value }
    val lowestBalance = points.minByOrNull { it.value }

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
                text = "Balance Trend Chart",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Daily balance over the last 14 days",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (points.all { it.value == 0.0 }) {
                Text(
                    text = "No balance data for this period.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                SimpleLineChart(
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
                    text = "Latest balance: ${formatCompactCurrency(latestBalance)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (latestBalance >= 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )

                if (highestBalance != null) {
                    Text(
                        text = "Highest: ${highestBalance.label} - ${formatCompactCurrency(highestBalance.value)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (lowestBalance != null) {
                    Text(
                        text = "Lowest: ${lowestBalance.label} - ${formatCompactCurrency(lowestBalance.value)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}