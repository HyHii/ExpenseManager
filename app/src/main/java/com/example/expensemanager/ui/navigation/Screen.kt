package com.example.expensemanager.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Transactions : Screen("transactions")
    object Scheduled : Screen("scheduled")
    object Analytics : Screen("analytics")
    object Goals : Screen("goals")

    object Income : Screen("income")
    object Expense : Screen("expense")
    object AddIncome : Screen("add_income")
    object AddExpense : Screen("add_expense")
    object AddScheduled : Screen("add_scheduled")
    object History : Screen("history")
    object SetBudget : Screen("set_budget")
    object SetSavingGoal : Screen("set_saving_goal")
    object CurrencySetting : Screen("currency_setting")
    object Settings : Screen("settings")

    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Int): String {
            return "edit_transaction/$transactionId"
        }
    }
}