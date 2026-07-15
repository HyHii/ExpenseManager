package com.example.expensemanager.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanager.data.local.ScheduledTransactionEntity
import com.example.expensemanager.ui.navigation.Screen
import com.example.expensemanager.utils.ScheduledFrequency
import com.example.expensemanager.utils.formatCurrency
import com.example.expensemanager.utils.formatDate
import com.example.expensemanager.viewmodel.ScheduledTransactionViewModel
import com.example.expensemanager.viewmodel.TransactionViewModel
import kotlin.math.ceil

private data class DashboardUiState(
    val totalIncome: Double,
    val totalExpense: Double,
    val currentBalance: Double,
    val upcomingExpenseTotal: Double,
    val predictedBalance: Double,
    val upcomingScheduledExpenses: List<ScheduledTransactionEntity>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    scheduledTransactionViewModel: ScheduledTransactionViewModel,
    monthlyBudget: Double,
    savingGoalName: String,
    savingGoalTargetAmount: Double,
    notifiedBadgeIds: Set<String>,
    onBadgeNotified: (String) -> Unit,
    onMenuClick: () -> Unit = {}
) {
    val totalIncome = transactionViewModel.totalIncome()
    val totalExpense = transactionViewModel.totalExpense()
    val currentBalance = transactionViewModel.currentBalance()

    val scheduledUpcomingExpenses = scheduledTransactionViewModel.upcomingReminders()
    val scheduledUpcomingTotal = scheduledUpcomingExpenses.sumOf { it.amount }

    val predictedBalance = currentBalance - scheduledUpcomingTotal

    val state = DashboardUiState(
        totalIncome = totalIncome,
        totalExpense = totalExpense,
        currentBalance = currentBalance,
        upcomingExpenseTotal = scheduledUpcomingTotal,
        predictedBalance = predictedBalance,
        upcomingScheduledExpenses = scheduledUpcomingExpenses
    )

    DashboardContent(
        state = state,
        onMenuClick = onMenuClick,
        onAddIncomeClick = {
            navController.navigate(Screen.AddIncome.route)
        },
        onAddExpenseClick = {
            navController.navigate(Screen.AddExpense.route)
        },
        onScheduledClick = {
            navController.navigate(Screen.Scheduled.route)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardContent(
    state: DashboardUiState,
    onMenuClick: () -> Unit,
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onScheduledClick: () -> Unit
) {
    var addMenuExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = {
                        addMenuExpanded = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add transaction"
                    )
                }

                DropdownMenu(
                    expanded = addMenuExpanded,
                    onDismissRequest = {
                        addMenuExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Add Income")
                        },
                        onClick = {
                            addMenuExpanded = false
                            onAddIncomeClick()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Add Expense")
                        },
                        onClick = {
                            addMenuExpanded = false
                            onAddExpenseClick()
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MainBalanceCard(
                    balance = state.currentBalance
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "Income",
                        amount = state.totalIncome,
                        icon = Icons.Default.KeyboardArrowUp
                    )

                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "Expense",
                        amount = state.totalExpense,
                        icon = Icons.Default.KeyboardArrowDown
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "Upcoming",
                        amount = state.upcomingExpenseTotal,
                        icon = Icons.Default.DateRange
                    )

                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "Predicted",
                        amount = state.predictedBalance,
                        icon = Icons.Default.Warning
                    )
                }
            }

            item {
                Text(
                    text = "Upcoming Expenses",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                HorizontalDivider()
            }

            if (state.upcomingScheduledExpenses.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "No upcoming expenses.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                items(state.upcomingScheduledExpenses) { scheduledTransaction ->
                    UpcomingScheduledExpenseCard(
                        scheduledTransaction = scheduledTransaction,
                        onClick = onScheduledClick
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun MainBalanceCard(
    balance: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null
                )

                Text(
                    text = "Current Balance",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = formatCurrency(balance),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = formatCurrency(amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun UpcomingScheduledExpenseCard(
    scheduledTransaction: ScheduledTransactionEntity,
    onClick: () -> Unit
) {
    val daysLeft = calculateDaysLeft(scheduledTransaction.nextDueDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = scheduledTransaction.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = formatCurrency(scheduledTransaction.amount),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Category: ${scheduledTransaction.category}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Frequency: ${ScheduledFrequency.labelOf(scheduledTransaction.frequency)}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Due date: ${formatDate(scheduledTransaction.nextDueDate)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = when (daysLeft) {
                        0L -> "Due today"
                        1L -> "Due in 1 day"
                        else -> "Due in $daysLeft days"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun calculateDaysLeft(
    dueDate: Long
): Long {
    val diff = dueDate - System.currentTimeMillis()
    val oneDay = 24 * 60 * 60 * 1000.0

    return ceil(diff / oneDay)
        .toLong()
        .coerceAtLeast(0)
}