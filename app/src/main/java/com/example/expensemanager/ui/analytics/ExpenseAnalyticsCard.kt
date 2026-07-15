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
import com.example.expensemanager.utils.ExpenseCategorySummary
import com.example.expensemanager.utils.formatCurrency

@Composable
fun ExpenseAnalyticsCard(
    categorySummaries: List<ExpenseCategorySummary>
) {
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
                text = "Expense Analytics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (categorySummaries.isEmpty()) {
                Text(
                    text = "No expense data yet.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                categorySummaries.take(5).forEachIndexed { index, summary ->
                    ExpenseCategoryRow(
                        rank = index + 1,
                        summary = summary
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun ExpenseCategoryRow(
    rank: Int,
    summary: ExpenseCategorySummary
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$rank. ${summary.category}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${formatCurrency(summary.totalAmount)} • ${(summary.percentage * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { summary.percentage.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}