package com.example.expensemanager.ui.goal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expensemanager.utils.formatCurrency

@Composable
fun SavingGoalCard(
    goalName: String,
    targetAmount: Double,
    currentBalance: Double,
    onClick: () -> Unit
) {
    val hasGoal = goalName.isNotBlank() && targetAmount > 0
    val savedAmount = currentBalance.coerceAtLeast(0.0)

    val progress = if (hasGoal) {
        (savedAmount / targetAmount).coerceIn(0.0, 1.0).toFloat()
    } else {
        0f
    }

    val remaining = if (hasGoal) {
        (targetAmount - savedAmount).coerceAtLeast(0.0)
    } else {
        0.0
    }

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
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Saving Goal"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (hasGoal) "Saving Goal: $goalName" else "Saving Goal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (hasGoal) {
                Text(
                    text = "${formatCurrency(savedAmount)} / ${formatCurrency(targetAmount)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (remaining <= 0) {
                        "Goal completed!"
                    } else {
                        "Remaining: ${formatCurrency(remaining)}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (remaining <= 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            } else {
                Text(
                    text = "Tap to set your first saving goal",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}