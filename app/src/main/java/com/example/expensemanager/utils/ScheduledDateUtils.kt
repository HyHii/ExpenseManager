package com.example.expensemanager.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object ScheduledFrequency {
    const val DAILY = "daily"
    const val WEEKLY = "weekly"
    const val MONTHLY = "monthly"
    const val YEARLY = "yearly"

    val all = listOf(
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    )

    fun labelOf(frequency: String): String {
        return when (frequency) {
            DAILY -> "Daily"
            WEEKLY -> "Weekly"
            MONTHLY -> "Monthly"
            YEARLY -> "Yearly"
            else -> frequency
        }
    }
}

fun calculateNextDueDate(
    currentDueDate: Long,
    frequency: String
): Long {
    val zoneId = ZoneId.systemDefault()

    val currentDate = Instant.ofEpochMilli(currentDueDate)
        .atZone(zoneId)
        .toLocalDate()

    val nextDate = when (frequency) {
        ScheduledFrequency.DAILY -> currentDate.plusDays(1)
        ScheduledFrequency.WEEKLY -> currentDate.plusWeeks(1)
        ScheduledFrequency.MONTHLY -> currentDate.plusMonths(1)
        ScheduledFrequency.YEARLY -> currentDate.plusYears(1)
        else -> currentDate.plusMonths(1)
    }

    return nextDate
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()
}

fun isReminderVisible(
    nextDueDate: Long,
    reminderDaysBefore: Int
): Boolean {
    val now = System.currentTimeMillis()
    val reminderStartDate = nextDueDate - reminderDaysBefore.coerceAtLeast(0) * 24L * 60L * 60L * 1000L

    return now >= reminderStartDate
}

fun getDueStatusText(
    nextDueDate: Long
): String {
    val zoneId = ZoneId.systemDefault()

    val today = LocalDate.now(zoneId)

    val dueDate = Instant.ofEpochMilli(nextDueDate)
        .atZone(zoneId)
        .toLocalDate()

    val daysBetween = ChronoUnit.DAYS.between(today, dueDate)

    return when {
        daysBetween < 0 -> "Overdue by ${-daysBetween} day(s)"
        daysBetween == 0L -> "Due today"
        daysBetween == 1L -> "Due tomorrow"
        else -> "Due in $daysBetween days"
    }
}