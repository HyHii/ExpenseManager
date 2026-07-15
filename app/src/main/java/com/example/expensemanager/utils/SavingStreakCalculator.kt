package com.example.expensemanager.utils

import com.example.expensemanager.model.Transaction
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

data class SavingStreakState(
    val streakDays: Int,
    val dailyLimit: Double,
    val todaySpent: Double,
    val freezeAllowance: Int,
    val freezeUsed: Int,
    val freezeLeft: Int,
    val isTodayOverLimit: Boolean,
    val isTodayProtected: Boolean
)

fun calculateSavingStreak(
    transactions: List<Transaction>,
    monthlyBudget: Double,
    freezeAllowance: Int = 2
): SavingStreakState {
    if (monthlyBudget <= 0) {
        return SavingStreakState(
            streakDays = 0,
            dailyLimit = 0.0,
            todaySpent = 0.0,
            freezeAllowance = freezeAllowance,
            freezeUsed = 0,
            freezeLeft = freezeAllowance,
            isTodayOverLimit = false,
            isTodayProtected = false
        )
    }

    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now()
    val currentMonth = YearMonth.from(today)
    val firstDayOfMonth = currentMonth.atDay(1)
    val dailyLimit = monthlyBudget / currentMonth.lengthOfMonth()

    val expenseByDate = transactions
        .filter { it.type == "expense" }
        .mapNotNull { transaction ->
            val date = Instant.ofEpochMilli(transaction.date)
                .atZone(zoneId)
                .toLocalDate()

            if (YearMonth.from(date) == currentMonth) {
                date to transaction.amount
            } else {
                null
            }
        }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )
        .mapValues { entry ->
            entry.value.sum()
        }

    val todaySpent = expenseByDate[today] ?: 0.0
    val isTodayOverLimit = todaySpent > dailyLimit

    var streakDays = 0
    var freezeUsed = 0
    var currentDate = today
    var isTodayProtected = false

    while (!currentDate.isBefore(firstDayOfMonth)) {
        val spent = expenseByDate[currentDate] ?: 0.0

        if (spent <= dailyLimit) {
            streakDays++
        } else {
            if (freezeUsed < freezeAllowance) {
                streakDays++
                freezeUsed++

                if (currentDate == today) {
                    isTodayProtected = true
                }
            } else {
                break
            }
        }

        currentDate = currentDate.minusDays(1)
    }

    return SavingStreakState(
        streakDays = streakDays,
        dailyLimit = dailyLimit,
        todaySpent = todaySpent,
        freezeAllowance = freezeAllowance,
        freezeUsed = freezeUsed,
        freezeLeft = (freezeAllowance - freezeUsed).coerceAtLeast(0),
        isTodayOverLimit = isTodayOverLimit,
        isTodayProtected = isTodayProtected
    )
}