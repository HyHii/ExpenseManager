package com.example.expensemanager.utils

import com.example.expensemanager.model.Transaction

data class AchievementBadge(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean
)

fun calculateAchievements(
    transactions: List<Transaction>,
    totalExpense: Double,
    currentBalance: Double,
    monthlyBudget: Double,
    savingGoalName: String,
    savingGoalTargetAmount: Double,
    savingStreakState: SavingStreakState
): List<AchievementBadge> {
    val hasTransactions = transactions.isNotEmpty()
    val hasSavingGoal = savingGoalName.isNotBlank() && savingGoalTargetAmount > 0
    val isGoalCompleted = hasSavingGoal && currentBalance >= savingGoalTargetAmount
    val isWithinBudget = monthlyBudget > 0 && totalExpense <= monthlyBudget

    return listOf(
        AchievementBadge(
            id = "budget_master",
            title = "Budget Master",
            description = "You stayed within your monthly budget.",
            icon = "🏆",
            isUnlocked = hasTransactions && isWithinBudget
        ),
        AchievementBadge(
            id = "seven_day_saver",
            title = "7-Day Saver",
            description = "You kept your saving streak for 7 days.",
            icon = "🔥",
            isUnlocked = savingStreakState.streakDays >= 7
        ),
        AchievementBadge(
            id = "goal_chaser",
            title = "Goal Chaser",
            description = "You created your first saving goal.",
            icon = "🎯",
            isUnlocked = hasSavingGoal
        ),
        AchievementBadge(
            id = "goal_completed",
            title = "Goal Completed",
            description = "You reached your saving goal target.",
            icon = "🌟",
            isUnlocked = isGoalCompleted
        ),
        AchievementBadge(
            id = "tracking_pro",
            title = "Tracking Pro",
            description = "You recorded at least 20 transactions.",
            icon = "📒",
            isUnlocked = transactions.size >= 20
        ),
        AchievementBadge(
            id = "smart_spender",
            title = "Smart Spender",
            description = "You stayed within today's daily spending limit.",
            icon = "🧠",
            isUnlocked = savingStreakState.dailyLimit > 0 &&
                    savingStreakState.todaySpent <= savingStreakState.dailyLimit
        ),
        AchievementBadge(
            id = "freeze_survivor",
            title = "Freeze Survivor",
            description = "A streak freeze protected your saving streak.",
            icon = "🧊",
            isUnlocked = savingStreakState.freezeUsed > 0
        )
    )
}