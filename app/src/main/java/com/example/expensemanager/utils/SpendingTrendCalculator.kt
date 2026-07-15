package com.example.expensemanager.utils

import com.example.expensemanager.model.Transaction
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

data class SpendingTrendState(
    val currentMonthExpense: Double,
    val previousMonthExpense: Double,
    val changePercent: Float,
    val averageDailySpending: Double,
    val highestSpendingDay: String,
    val highestSpendingDayAmount: Double,
    val trendLabel: String,
    val insight: String
)

fun calculateSpendingTrend(
    transactions: List<Transaction>
): SpendingTrendState {
    val zoneId = ZoneId.systemDefault()
    val currentMonth = YearMonth.now()
    val previousMonth = currentMonth.minusMonths(1)
    val today = LocalDate.now()

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    val expenses = transactions.filter { it.type == "expense" }

    fun transactionMonth(transaction: Transaction): YearMonth {
        val date = Instant.ofEpochMilli(transaction.date)
            .atZone(zoneId)
            .toLocalDate()

        return YearMonth.from(date)
    }

    fun transactionDate(transaction: Transaction): LocalDate {
        return Instant.ofEpochMilli(transaction.date)
            .atZone(zoneId)
            .toLocalDate()
    }

    val currentMonthExpenses = expenses.filter {
        transactionMonth(it) == currentMonth
    }

    val previousMonthExpenses = expenses.filter {
        transactionMonth(it) == previousMonth
    }

    val currentTotal = currentMonthExpenses.sumOf { it.amount }
    val previousTotal = previousMonthExpenses.sumOf { it.amount }

    val changePercent = when {
        previousTotal > 0 -> ((currentTotal - previousTotal) / previousTotal).toFloat()
        previousTotal == 0.0 && currentTotal > 0 -> 1f
        else -> 0f
    }

    val averageDailySpending = if (today.dayOfMonth > 0) {
        currentTotal / today.dayOfMonth
    } else {
        0.0
    }

    val highestDayEntry = currentMonthExpenses
        .groupBy { transactionDate(it) }
        .mapValues { entry ->
            entry.value.sumOf { it.amount }
        }
        .maxByOrNull { it.value }

    val highestDay = highestDayEntry?.key?.format(dateFormatter) ?: "No spending day yet"
    val highestDayAmount = highestDayEntry?.value ?: 0.0

    val trendLabel = when {
        currentTotal == 0.0 && previousTotal == 0.0 -> "No spending data yet"
        changePercent > 0.1f -> "Spending increased"
        changePercent < -0.1f -> "Spending decreased"
        else -> "Spending is stable"
    }

    val insight = when {
        currentTotal == 0.0 -> "No expenses recorded this month yet."
        previousTotal == 0.0 -> "This is your first month with spending data."
        changePercent > 0.25f -> "Your spending is rising quickly compared with last month."
        changePercent > 0.1f -> "Your spending is slightly higher than last month."
        changePercent < -0.25f -> "Great job. Your spending is much lower than last month."
        changePercent < -0.1f -> "Your spending is lower than last month."
        else -> "Your spending pattern is quite stable."
    }

    return SpendingTrendState(
        currentMonthExpense = currentTotal,
        previousMonthExpense = previousTotal,
        changePercent = changePercent,
        averageDailySpending = averageDailySpending,
        highestSpendingDay = highestDay,
        highestSpendingDayAmount = highestDayAmount,
        trendLabel = trendLabel,
        insight = insight
    )
}