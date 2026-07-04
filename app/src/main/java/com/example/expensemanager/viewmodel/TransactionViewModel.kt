package com.example.expensemanager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.data.local.TransactionEntity
import com.example.expensemanager.data.local.toModel
import com.example.expensemanager.data.repository.TransactionRepository
import com.example.expensemanager.model.Transaction
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repo: TransactionRepository
) : ViewModel() {

    private var allCache by mutableStateOf<List<Transaction>>(emptyList())
    private var expenseCache by mutableStateOf<List<Transaction>>(emptyList())
    private var incomeCache by mutableStateOf<List<Transaction>>(emptyList())

    init {
        viewModelScope.launch {
            repo.all.collect { list ->
                allCache = list.map { it.toModel() }
            }
        }

        viewModelScope.launch {
            repo.expenses.collect { list ->
                expenseCache = list.map { it.toModel() }
            }
        }

        viewModelScope.launch {
            repo.income.collect { list ->
                incomeCache = list.map { it.toModel() }
            }
        }
    }

    fun allTransactions(): List<Transaction> = allCache

    fun expenseList(): List<Transaction> = expenseCache

    fun incomeList(): List<Transaction> = incomeCache

    fun addExpense(
        title: String,
        amount: Double,
        category: String,
        date: Long,
        note: String
    ) {
        viewModelScope.launch {
            repo.insert(
                TransactionEntity(
                    title = title,
                    amount = amount,
                    category = category,
                    date = date,
                    note = note,
                    type = "expense"
                )
            )
        }
    }

    fun addIncome(
        title: String,
        amount: Double,
        category: String,
        date: Long,
        note: String
    ) {
        viewModelScope.launch {
            repo.insert(
                TransactionEntity(
                    title = title,
                    amount = amount,
                    category = category,
                    date = date,
                    note = note,
                    type = "income"
                )
            )
        }
    }

    fun totalIncome(): Double = incomeCache.sumOf { it.amount }

    fun totalExpense(): Double = expenseCache.sumOf { it.amount }

    fun currentBalance(): Double = totalIncome() - totalExpense()

    fun upcomingExpenses(): List<Transaction> {
        val now = System.currentTimeMillis()
        return expenseCache.filter { it.date > now }.sortedBy { it.date }
    }

    fun upcomingExpenseTotal(): Double = upcomingExpenses().sumOf { it.amount }

    fun predictedBalance(): Double = currentBalance() - upcomingExpenseTotal()

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repo.update(
                TransactionEntity(
                    id = transaction.id,
                    title = transaction.title,
                    amount = transaction.amount,
                    category = transaction.category,
                    date = transaction.date,
                    note = transaction.note,
                    type = transaction.type
                )
            )
        }
    }
    suspend fun getTransactionById(id: Int): Transaction? {
        return repo.getTransactionById(id)?.toModel()
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repo.delete(
                TransactionEntity(
                    id = transaction.id,
                    title = transaction.title,
                    amount = transaction.amount,
                    category = transaction.category,
                    date = transaction.date,
                    note = transaction.note,
                    type = transaction.type
                )
            )
        }
    }

}