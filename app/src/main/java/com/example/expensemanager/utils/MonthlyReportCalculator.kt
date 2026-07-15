package com.example.expensemanager.utils

import com.example.expensemanager.model.Transaction
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

data class MonthlyReportState(
    val monthLabel: String,
    val totalIncome: Double,
    val totalExpense: Double,
    val netSaving: Double,
    val savingRate: Float,
    val topCategory: String,
    val topCategoryAmount: Double,
    val budgetStatus: String,
    val verdict: String
)

fun calculateMonthlyReport(
    transactions: List<Transaction>,
    monthlyBudget: Double
): MonthlyReportState {
    val zoneId = ZoneId.systemDefault()
    val currentMonth = YearMonth.now()
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    val transactionsThisMonth = transactions.filter { transaction ->
        val date = Instant.ofEpochMilli(transaction.date)
            .atZone(zoneId)
            .toLocalDate()

        YearMonth.from(date) == currentMonth
    }

    val incomeThisMonth = transactionsThisMonth
        .filter { it.type == "income" }
        .sumOf { it.amount }

    val expenseThisMonth = transactionsThisMonth
        .filter { it.type == "expense" }
        .sumOf { it.amount }

    val netSaving = incomeThisMonth - expenseThisMonth

    val savingRate = if (incomeThisMonth > 0) {
        (netSaving / incomeThisMonth).toFloat()
    } else {
        0f
    }

    val topExpenseCategory = transactionsThisMonth
        .filter { it.type == "expense" }
        .groupBy { it.category }
        .mapValues { entry ->
            entry.value.sumOf { it.amount }
        }
        .maxByOrNull { it.value }

    val budgetStatus = when {
        monthlyBudget <= 0 -> "No monthly budget set"
        expenseThisMonth <= monthlyBudget -> {
            "Under budget by ${formatCurrency(monthlyBudget - expenseThisMonth)}"
        }
        else -> {
            "Over budget by ${formatCurrency(abs(monthlyBudget - expenseThisMonth))}"
        }
    }

    val verdict = when {
        transactionsThisMonth.isEmpty() -> "No data for this month yet"
        monthlyBudget > 0 && expenseThisMonth > monthlyBudget -> "Spending is above your monthly budget"
        savingRate >= 0.5f -> "Great saving month"
        savingRate >= 0.2f -> "Good financial balance"
        savingRate >= 0f -> "Low saving month"
        else -> "You spent more than you earned"
    }

    return MonthlyReportState(
        monthLabel = currentMonth.format(formatter),
        totalIncome = incomeThisMonth,
        totalExpense = expenseThisMonth,
        netSaving = netSaving,
        savingRate = savingRate,
        topCategory = topExpenseCategory?.key ?: "No expense category yet",
        topCategoryAmount = topExpenseCategory?.value ?: 0.0,
        budgetStatus = budgetStatus,
        verdict = verdict
    )
}