package com.example.expensemanager.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Income : Screen("income")
    object Expense : Screen("expense")
    object AddIncome : Screen("add_income")
    object AddExpense : Screen("add_expense")
    object History : Screen("history")
    object SetBudget : Screen("set_budget")

    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Int): String {
            return "edit_transaction/$transactionId"
        }
    }
}