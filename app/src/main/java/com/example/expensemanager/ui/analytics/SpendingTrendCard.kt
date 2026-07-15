package com.example.expensemanager.ui.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expensemanager.utils.SpendingTrendState
import com.example.expensemanager.utils.formatCurrency
import kotlin.math.abs

@Composable
fun SpendingTrendCard(
    trend: SpendingTrendState
) {
    val absoluteChangePercent = abs(trend.changePercent * 100).toInt()

    val progress = when {
        trend.previousMonthExpense <= 0 && trend.currentMonthExpense > 0 -> 1f
        trend.previousMonthExpense <= 0 -> 0f
        else -> (trend.currentMonthExpense / trend.previousMonthExpense)
            .coerceIn(0.0, 1.0)
            .toFloat()
    }

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
                text = "Spending Trend",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = trend.trendLabel,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This month: ${formatCurrency(trend.currentMonthExpense)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Last month: ${formatCurrency(trend.previousMonthExpense)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = when {
                    trend.changePercent > 0 -> "Change: +$absoluteChangePercent%"
                    trend.changePercent < 0 -> "Change: -$absoluteChangePercent%"
                    else -> "Change: 0%"
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Average daily spending: ${formatCurrency(trend.averageDailySpending)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Highest spending day: ${trend.highestSpendingDay}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (trend.highestSpendingDayAmount > 0) {
                Text(
                    text = "Amount: ${formatCurrency(trend.highestSpendingDayAmount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Insight: ${trend.insight}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}