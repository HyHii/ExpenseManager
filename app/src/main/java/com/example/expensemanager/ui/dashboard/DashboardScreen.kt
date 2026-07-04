package com.example.expensemanager.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanager.model.Transaction
import com.example.expensemanager.ui.navigation.Screen
import com.example.expensemanager.utils.formatCurrency
import com.example.expensemanager.utils.formatDate
import com.example.expensemanager.viewmodel.TransactionViewModel
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.ui.Alignment
import com.example.expensemanager.ui.budget.BudgetProgressCard

data class DashboardUiState(
    val totalIncome: Double,
    val totalExpense: Double,
    val currentBalance: Double,
    val upcomingExpenseTotal: Double,
    val predictedBalance: Double,
    val monthlyBudget: Double,
    val monthlyBudgetRemaining: Double,
    val upcomingExpenses: List<Transaction>
)

@Composable
fun DashboardScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    monthlyBudget: Double
) {
    val state = DashboardUiState(
        totalIncome = transactionViewModel.totalIncome(),
        totalExpense = transactionViewModel.totalExpense(),
        currentBalance = transactionViewModel.currentBalance(),
        upcomingExpenseTotal = transactionViewModel.upcomingExpenseTotal(),
        predictedBalance = transactionViewModel.predictedBalance(),
        monthlyBudget = monthlyBudget,
        monthlyBudgetRemaining = monthlyBudget - transactionViewModel.totalExpense(),
        upcomingExpenses = transactionViewModel.upcomingExpenses()
    )

    DashboardContent(
        state = state,
        isDarkMode = isDarkMode,
        onDarkModeChange = onDarkModeChange,
        onIncomeClick = {
            navController.navigate(Screen.Income.route)
        },
        onExpenseClick = {
            navController.navigate(Screen.Expense.route)
        },
        onAddIncomeClick = {
            navController.navigate(Screen.AddIncome.route)
        },
        onAddExpenseClick = {
            navController.navigate(Screen.AddExpense.route)
        },
        onHistoryClick = {
            navController.navigate(Screen.History.route)
        },
        onBudgetClick = {
            navController.navigate(Screen.SetBudget.route)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    state: DashboardUiState,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onIncomeClick: () -> Unit,
    onExpenseClick: () -> Unit,
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onBudgetClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isDarkMode) "Dark" else "Light",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { checked ->
                                onDarkModeChange(checked)
                            }
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = { expanded = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Add Income") },
                        onClick = {
                            expanded = false
                            onAddIncomeClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Add Expense") },
                        onClick = {
                            expanded = false
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MainBalanceCard(balance = state.currentBalance)
            }
            item {
                BudgetProgressCard(
                    monthlyBudget = state.monthlyBudget,
                    totalExpense = state.totalExpense,
                    onClick = onBudgetClick
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "Income",
                        amount = state.totalIncome,
                        type = "income",
                        onClick = onIncomeClick
                    )

                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "Expense",
                        amount = state.totalExpense,
                        type = "expense",
                        onClick = onExpenseClick
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "Upcoming",
                        amount = state.upcomingExpenseTotal,
                        type = "upcoming"
                    )

                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "Predicted",
                        amount = state.predictedBalance,
                        type = "predicted"
                    )
                }
            }

            item {
                SectionHeader(title = "Upcoming Expenses")
            }

            items(state.upcomingExpenses) { expense ->
                UpcomingExpenseCard(expense = expense)
            }

            item {
                OutlinedButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Transaction History")
                }
            }
        }
    }
}

@Composable
fun MainBalanceCard(balance: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Current Balance"
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Current Balance",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = formatCurrency(balance),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    type: String,
    onClick: (() -> Unit)? = null
) {
    val icon = when (type) {
        "income" -> Icons.Default.KeyboardArrowUp
        "expense" -> Icons.Default.KeyboardArrowDown
        "budget" -> Icons.Default.Warning
        "upcoming" -> Icons.Default.DateRange
        else -> Icons.Default.Warning
    }

    Card(
        modifier = if (onClick != null) {
            modifier.clickable { onClick() }
        } else {
            modifier
        },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = formatCurrency(amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
    }
}

@Composable
fun UpcomingExpenseCard(expense: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = expense.category,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Amount: ${formatCurrency(expense.amount)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Date: ${formatDate(expense.date)}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (expense.note.isNotBlank()) {
                Text(
                    text = "Note: ${expense.note}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}