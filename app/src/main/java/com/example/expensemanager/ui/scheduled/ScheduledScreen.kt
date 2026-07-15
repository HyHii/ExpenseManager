package com.example.expensemanager.ui.scheduled

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanager.data.local.ScheduledTransactionEntity
import com.example.expensemanager.ui.navigation.Screen
import com.example.expensemanager.utils.ScheduledFrequency
import com.example.expensemanager.utils.formatCurrency
import com.example.expensemanager.utils.formatDate
import com.example.expensemanager.utils.getDueStatusText
import com.example.expensemanager.viewmodel.ScheduledTransactionViewModel
import com.example.expensemanager.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledScreen(
    navController: NavController,
    scheduledTransactionViewModel: ScheduledTransactionViewModel,
    transactionViewModel: TransactionViewModel,
    onMenuClick: () -> Unit = {}
) {
    val upcomingReminders = scheduledTransactionViewModel.upcomingReminders()
    val allScheduled = scheduledTransactionViewModel.allScheduled()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open menu"
                        )
                    }
                },
                title = {
                    Text(
                        text = "Scheduled",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Button(
                    onClick = {
                        navController.navigate(Screen.AddScheduled.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Scheduled Transaction")
                }
            }

            item {
                SectionTitle(title = "Upcoming Reminders")
            }

            if (upcomingReminders.isEmpty()) {
                item {
                    EmptyScheduledCard(
                        message = "No upcoming reminders."
                    )
                }
            } else {
                items(upcomingReminders) { scheduledTransaction ->
                    ScheduledReminderCard(
                        scheduledTransaction = scheduledTransaction,
                        onMarkAsPaid = {
                            markScheduledAsPaid(
                                scheduledTransaction = scheduledTransaction,
                                transactionViewModel = transactionViewModel,
                                scheduledTransactionViewModel = scheduledTransactionViewModel
                            )
                        }
                    )
                }
            }

            item {
                SectionTitle(title = "Recurring Transactions")
            }

            if (allScheduled.isEmpty()) {
                item {
                    EmptyScheduledCard(
                        message = "No recurring transactions yet."
                    )
                }
            } else {
                items(allScheduled) { scheduledTransaction ->
                    ScheduledTransactionCard(
                        scheduledTransaction = scheduledTransaction,
                        onToggleActive = {
                            scheduledTransactionViewModel.toggleActive(scheduledTransaction)
                        },
                        onDelete = {
                            scheduledTransactionViewModel.deleteScheduledTransaction(scheduledTransaction)
                        }
                    )
                }
            }
        }
    }
}

private fun markScheduledAsPaid(
    scheduledTransaction: ScheduledTransactionEntity,
    transactionViewModel: TransactionViewModel,
    scheduledTransactionViewModel: ScheduledTransactionViewModel
) {
    val paidDate = System.currentTimeMillis()

    if (scheduledTransaction.type == "income") {
        transactionViewModel.addIncome(
            title = scheduledTransaction.title,
            amount = scheduledTransaction.amount,
            category = scheduledTransaction.category,
            date = paidDate,
            note = scheduledTransaction.note
        )
    } else {
        transactionViewModel.addExpense(
            title = scheduledTransaction.title,
            amount = scheduledTransaction.amount,
            category = scheduledTransaction.category,
            date = paidDate,
            note = scheduledTransaction.note
        )
    }

    scheduledTransactionViewModel.moveToNextCycle(scheduledTransaction)
}

@Composable
private fun ScheduledReminderCard(
    scheduledTransaction: ScheduledTransactionEntity,
    onMarkAsPaid: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Reminder"
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = getDueStatusText(scheduledTransaction.nextDueDate),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = scheduledTransaction.title,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatCurrency(scheduledTransaction.amount)
            )

            Text(
                text = "${ScheduledFrequency.labelOf(scheduledTransaction.frequency)} • ${scheduledTransaction.type}"
            )

            Text(
                text = "Due date: ${formatDate(scheduledTransaction.nextDueDate)}"
            )

            if (scheduledTransaction.note.isNotBlank()) {
                Text(
                    text = "Note: ${scheduledTransaction.note}"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onMarkAsPaid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mark as paid")
            }
        }
    }
}

@Composable
private fun ScheduledTransactionCard(
    scheduledTransaction: ScheduledTransactionEntity,
    onToggleActive: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = scheduledTransaction.title,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatCurrency(scheduledTransaction.amount)
            )

            Text(
                text = "Category: ${scheduledTransaction.category}"
            )

            Text(
                text = "Type: ${scheduledTransaction.type}"
            )

            Text(
                text = "Frequency: ${ScheduledFrequency.labelOf(scheduledTransaction.frequency)}"
            )

            Text(
                text = "Next due: ${formatDate(scheduledTransaction.nextDueDate)}"
            )

            Text(
                text = "Reminder: ${scheduledTransaction.reminderDaysBefore} day(s) before"
            )

            Text(
                text = if (scheduledTransaction.isActive) "Status: Active" else "Status: Paused"
            )

            if (scheduledTransaction.note.isNotBlank()) {
                Text(
                    text = "Note: ${scheduledTransaction.note}"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onToggleActive,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (scheduledTransaction.isActive) "Pause" else "Resume"
                    )
                }

                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String
) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun EmptyScheduledCard(
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp)
        )
    }
}