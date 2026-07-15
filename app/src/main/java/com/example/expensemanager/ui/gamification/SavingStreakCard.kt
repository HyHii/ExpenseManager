package com.example.expensemanager.ui.gamification

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
import com.example.expensemanager.utils.SavingStreakState
import com.example.expensemanager.utils.formatCurrency

@Composable
fun SavingStreakCard(
    state: SavingStreakState
) {
    val progress = if (state.dailyLimit > 0) {
        (state.todaySpent / state.dailyLimit).coerceIn(0.0, 1.0).toFloat()
    } else {
        0f
    }

    val statusText = when {
        state.dailyLimit <= 0 -> "Set a monthly budget to start your saving streak."
        state.isTodayProtected -> "You exceeded today's limit, but 1 freeze protected your streak."
        state.isTodayOverLimit -> "Today is over the daily limit."
        else -> "You are within today's spending limit."
    }

    val statusColor = when {
        state.isTodayProtected -> MaterialTheme.colorScheme.primary
        state.isTodayOverLimit -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
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
                text = "Saving Streak",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🔥 ${state.streakDays} days",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Daily limit: ${formatCurrency(state.dailyLimit)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Today spent: ${formatCurrency(state.todaySpent)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🧊 Freeze left: ${state.freezeLeft}/${state.freezeAllowance}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium,
                color = statusColor
            )
        }
    }
}