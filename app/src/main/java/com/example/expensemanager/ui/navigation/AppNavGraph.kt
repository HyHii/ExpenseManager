package com.example.expensemanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensemanager.data.local.AppDatabase
import com.example.expensemanager.data.repository.TransactionRepository
import com.example.expensemanager.ui.dashboard.DashboardScreen
import com.example.expensemanager.ui.transaction.screen.ExpenseScreen
import com.example.expensemanager.ui.transaction.screen.IncomeScreen
import com.example.expensemanager.ui.history.HistoryScreen
import com.example.expensemanager.ui.transaction.action.AddExpenseScreen
import com.example.expensemanager.ui.transaction.action.AddIncomeScreen
import com.example.expensemanager.viewmodel.TransactionViewModel
import com.example.expensemanager.ui.transaction.action.EditTransactionScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.expensemanager.ui.budget.SetBudgetScreen

@Composable
fun AppNavGraph(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    monthlyBudget: Double,
    onMonthlyBudgetChange: (Double) -> Unit
) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repo = TransactionRepository(db.transactionDao())

    val transactionViewModel: TransactionViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(repo) as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                navController = navController,
                transactionViewModel = transactionViewModel,
                isDarkMode = isDarkMode,
                onDarkModeChange = onDarkModeChange,
                monthlyBudget = monthlyBudget
            )
        }

        composable(Screen.AddIncome.route) {
            AddIncomeScreen(
                navController = navController,
                transactionViewModel = transactionViewModel
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                navController = navController,
                transactionViewModel = transactionViewModel
            )
        }

        composable(Screen.Expense.route) {
            ExpenseScreen(
                navController = navController,
                transactionViewModel = transactionViewModel
            )
        }

        composable(Screen.Income.route) {
            IncomeScreen(
                navController = navController,
                transactionViewModel = transactionViewModel
            )
        }

        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                navController = navController,
                transactionViewModel = transactionViewModel
            )
        }

        composable(
            route = Screen.EditTransaction.route,
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: return@composable

            EditTransactionScreen(
                navController = navController,
                transactionId = transactionId,
                transactionViewModel = transactionViewModel
            )
        }
        composable(Screen.SetBudget.route) {
            SetBudgetScreen(
                navController = navController,
                currentBudget = monthlyBudget,
                onSaveBudget = onMonthlyBudgetChange
            )
        }
    }
}