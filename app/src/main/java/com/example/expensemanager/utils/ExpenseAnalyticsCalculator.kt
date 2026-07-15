package com.example.expensemanager.utils

import com.example.expensemanager.model.Transaction

data class ExpenseCategorySummary(
    val category: String,
    val totalAmount: Double,
    val percentage: Float
)

fun calculateExpenseAnalytics(
    transactions: List<Transaction>
): List<ExpenseCategorySummary> {
    val expenseTransactions = transactions.filter { it.type == "expense" }

    val totalExpense = expenseTransactions.sumOf { it.amount }

    if (totalExpense <= 0) {
        return emptyList()
    }

    return expenseTransactions
        .groupBy { it.category }
        .map { (category, expenses) ->
            val categoryTotal = expenses.sumOf { it.amount }

            ExpenseCategorySummary(
                category = category,
                totalAmount = categoryTotal,
                percentage = (categoryTotal / totalExpense).toFloat()
            )
        }
        .sortedByDescending { it.totalAmount }
}