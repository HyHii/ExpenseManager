package com.example.expensemanager.utils

import com.example.expensemanager.model.Transaction
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ChartPoint(
    val label: String,
    val value: Double
)

fun calculateDailySpendingChartData(
    transactions: List<Transaction>,
    days: Int = 14
): List<ChartPoint> {
    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zoneId)
    val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())

    val expensesByDate = transactions
        .filter { it.type == "expense" }
        .groupBy { transaction ->
            Instant.ofEpochMilli(transaction.date)
                .atZone(zoneId)
                .toLocalDate()
        }
        .mapValues { entry ->
            entry.value.sumOf { it.amount }
        }

    return (days - 1 downTo 0).map { offset ->
        val date = today.minusDays(offset.toLong())

        ChartPoint(
            label = date.format(formatter),
            value = expensesByDate[date] ?: 0.0
        )
    }
}

data class IncomeExpenseChartPoint(
    val label: String,
    val income: Double,
    val expense: Double
)

fun calculateDailyIncomeExpenseChartData(
    transactions: List<Transaction>,
    days: Int = 14
): List<IncomeExpenseChartPoint> {
    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zoneId)
    val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())

    val incomeByDate = transactions
        .filter { it.type == "income" }
        .groupBy { transaction ->
            Instant.ofEpochMilli(transaction.date)
                .atZone(zoneId)
                .toLocalDate()
        }
        .mapValues { entry ->
            entry.value.sumOf { it.amount }
        }

    val expenseByDate = transactions
        .filter { it.type == "expense" }
        .groupBy { transaction ->
            Instant.ofEpochMilli(transaction.date)
                .atZone(zoneId)
                .toLocalDate()
        }
        .mapValues { entry ->
            entry.value.sumOf { it.amount }
        }

    return (days - 1 downTo 0).map { offset ->
        val date = today.minusDays(offset.toLong())

        IncomeExpenseChartPoint(
            label = date.format(formatter),
            income = incomeByDate[date] ?: 0.0,
            expense = expenseByDate[date] ?: 0.0
        )
    }
}

fun calculateDailyBalanceChartData(
    transactions: List<Transaction>,
    days: Int = 14
): List<ChartPoint> {
    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zoneId)
    val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())

    fun transactionDate(transaction: Transaction): LocalDate {
        return Instant.ofEpochMilli(transaction.date)
            .atZone(zoneId)
            .toLocalDate()
    }

    return (days - 1 downTo 0).map { offset ->
        val date = today.minusDays(offset.toLong())

        val balanceAtDate = transactions
            .filter { transactionDate(it) <= date }
            .sumOf { transaction ->
                if (transaction.type == "income") {
                    transaction.amount
                } else {
                    -transaction.amount
                }
            }

        ChartPoint(
            label = date.format(formatter),
            value = balanceAtDate
        )
    }
}