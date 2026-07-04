package com.example.expensemanager.ui.budget

import androidx.compose.foundation.clickable
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
import com.example.expensemanager.utils.formatCurrency
import kotlin.math.abs

@Composable
fun BudgetProgressCard(
    monthlyBudget: Double,
    totalExpense: Double,
    onClick: () -> Unit
) {
    val usedPercent = if (monthlyBudget > 0) {
        (totalExpense / monthlyBudget).coerceIn(0.0, 1.0).toFloat()
    } else {
        0f
    }

    val remaining = monthlyBudget - totalExpense
    val isOverBudget = remaining < 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Monthly Budget",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${formatCurrency(totalExpense)} / ${formatCurrency(monthlyBudget)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { usedPercent },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isOverBudget) {
                    "Over budget by ${formatCurrency(abs(remaining))}"
                } else {
                    "Remaining: ${formatCurrency(remaining)}"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (isOverBudget) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}