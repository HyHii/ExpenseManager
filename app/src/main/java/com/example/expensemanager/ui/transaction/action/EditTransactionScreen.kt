package com.example.expensemanager.ui.transaction.action

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanager.model.Transaction
import com.example.expensemanager.viewmodel.TransactionViewModel
import com.example.expensemanager.ui.components.CategoryGroupDropdown
import com.example.expensemanager.utils.expenseCategoryGroups
import com.example.expensemanager.utils.incomeCategoryGroups

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    navController: NavController,
    transactionId: Int,
    transactionViewModel: TransactionViewModel
) {
    var transaction by remember { mutableStateOf<Transaction?>(null) }

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(transactionId) {
        val loadedTransaction = transactionViewModel.getTransactionById(transactionId)
        transaction = loadedTransaction

        loadedTransaction?.let {
            amount = it.amount.toString()
            category = it.category
            note = it.note

            val groups = if (it.type == "income") {
                incomeCategoryGroups
            } else {
                expenseCategoryGroups
            }

            selectedGroup = groups.find { group ->
                group.subCategories.contains(it.category)
            }?.name ?: groups.first().name
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Transaction",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val currentTransaction = transaction

        if (currentTransaction == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text("Transaction not found")
            }
        } else {
            val groups = if (currentTransaction.type == "income") {
                incomeCategoryGroups
            } else {
                expenseCategoryGroups
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        amountError = null
                    },
                    label = { Text("Amount") },
                    isError = amountError != null,
                    supportingText = {
                        if (amountError != null) {
                            Text(amountError!!)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                CategoryGroupDropdown(
                    categoryGroups = groups,
                    selectedGroup = selectedGroup,
                    selectedSubCategory = category,
                    onGroupSelected = { selectedGroup = it },
                    onSubCategorySelected = { category = it },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val newAmount = amount.toDoubleOrNull()

                        amountError = when {
                            amount.isBlank() -> "Amount is required"
                            newAmount == null -> "Invalid amount"
                            newAmount <= 0 -> "Amount must be greater than 0"
                            else -> null
                        }

                        if (
                            amountError == null &&
                            category.isNotBlank()
                        ) {
                            val updatedTransaction = currentTransaction.copy(
                                title = category.trim(),
                                amount = newAmount!!,
                                category = category.trim(),
                                note = note.trim()
                            )

                            transactionViewModel.updateTransaction(updatedTransaction)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }

                OutlinedButton(
                    onClick = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete Transaction")
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                    },
                    title = {
                        Text("Delete Transaction")
                    },
                    text = {
                        Text("Are you sure you want to delete this transaction?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                transactionViewModel.deleteTransaction(currentTransaction)
                                showDeleteDialog = false
                                navController.popBackStack()
                            }
                        ) {
                            Text(
                                text = "Delete",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}