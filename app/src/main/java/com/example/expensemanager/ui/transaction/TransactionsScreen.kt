package com.example.expensemanager.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanager.model.Transaction
import com.example.expensemanager.ui.navigation.Screen
import com.example.expensemanager.utils.formatCurrency
import com.example.expensemanager.utils.formatDate
import com.example.expensemanager.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    onMenuClick: () -> Unit = {}
) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedType by remember {
        mutableStateOf("all")
    }

    val transactions = transactionViewModel.allTransactions()

    val filteredTransactions = transactions
        .filter { transaction ->
            when (selectedType) {
                "income" -> transaction.type == "income"
                "expense" -> transaction.type == "expense"
                else -> true
            }
        }
        .filter { transaction ->
            val query = searchQuery.trim().lowercase()

            if (query.isBlank()) {
                true
            } else {
                transaction.title.lowercase().contains(query) ||
                        transaction.category.lowercase().contains(query) ||
                        transaction.note.lowercase().contains(query) ||
                        transaction.amount.toString().contains(query)
            }
        }
        .sortedByDescending { it.date }

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
                        text = "Transactions",
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Search transaction")
                    },
                    singleLine = true
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TransactionFilterButton(
                        text = "All",
                        selected = selectedType == "all",
                        onClick = {
                            selectedType = "all"
                        }
                    )

                    TransactionFilterButton(
                        text = "Income",
                        selected = selectedType == "income",
                        onClick = {
                            selectedType = "income"
                        }
                    )

                    TransactionFilterButton(
                        text = "Expense",
                        selected = selectedType == "expense",
                        onClick = {
                            selectedType = "expense"
                        }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            navController.navigate(Screen.AddIncome.route)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add Income")
                    }

                    Button(
                        onClick = {
                            navController.navigate(Screen.AddExpense.route)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add Expense")
                    }
                }
            }

            if (filteredTransactions.isEmpty()) {
                item {
                    EmptyTransactionMessage()
                }
            } else {
                items(filteredTransactions) { transaction ->
                    TransactionListCard(
                        transaction = transaction,
                        onClick = {
                            navController.navigate(
                                Screen.EditTransaction.createRoute(transaction.id)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionFilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick
        ) {
            Text(text)
        }
    }
}

@Composable
private fun TransactionListCard(
    transaction: Transaction,
    onClick: () -> Unit
) {
    val isIncome = transaction.type == "income"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isIncome) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = transaction.type
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = transaction.category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = formatDate(transaction.date),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = if (isIncome) {
                        "+${formatCurrency(transaction.amount)}"
                    } else {
                        "-${formatCurrency(transaction.amount)}"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isIncome) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }

            if (transaction.note.isNotBlank()) {
                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyTransactionMessage() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = "No transactions found.",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}