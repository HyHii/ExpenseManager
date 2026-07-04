package com.example.expensemanager.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanager.model.Transaction
import com.example.expensemanager.ui.navigation.Screen
import com.example.expensemanager.utils.formatCurrency
import com.example.expensemanager.utils.formatDate
import com.example.expensemanager.viewmodel.TransactionViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    val transactions = transactionViewModel.allTransactions()

    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("All") }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredTransactions = transactions.filter { transaction ->
        val query = searchQuery.trim()

        val matchesType =
            selectedType == "All" ||
                    transaction.type.equals(selectedType, ignoreCase = true)

        val matchesCategory =
            selectedCategory == "All" ||
                    transaction.category == selectedCategory

        val matchesSearch =
            query.isBlank() ||
                    transaction.category.contains(query, ignoreCase = true) ||
                    transaction.note.contains(query, ignoreCase = true) ||
                    transaction.amount.toString().contains(query, ignoreCase = true) ||
                    formatDate(transaction.date).contains(query, ignoreCase = true)

        matchesType && matchesCategory && matchesSearch
    }

    val availableCategories = listOf("All") +
            transactions
                .map { it.category }
                .distinct()
                .sorted()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Transaction History", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search transaction") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Filter by type",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Filter by category",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableCategories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "income", "expense").forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = {
                            Text(
                                when (type) {
                                    "income" -> "Income"
                                    "expense" -> "Expense"
                                    else -> "All"
                                }
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    TransactionHistoryCard(
                        transaction = transaction,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionHistoryCard(
    transaction: Transaction,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.EditTransaction.createRoute(transaction.id))
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = transaction.category,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Type: ${transaction.type}")
            Text("Amount: ${formatCurrency(transaction.amount)}")
            Text("Date: ${formatDate(transaction.date)}")

            if (transaction.note.isNotBlank()) {
                Text("Note: ${transaction.note}")
            }
        }
    }
}