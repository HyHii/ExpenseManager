package com.example.expensemanager.ui.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensemanager.data.local.AppDatabase
import com.example.expensemanager.data.repository.ScheduledTransactionRepository
import com.example.expensemanager.data.repository.TransactionRepository
import com.example.expensemanager.ui.analytics.AnalyticsScreen
import com.example.expensemanager.ui.budget.SetBudgetScreen
import com.example.expensemanager.ui.dashboard.DashboardScreen
import com.example.expensemanager.ui.goal.GoalsScreen
import com.example.expensemanager.ui.goal.SetSavingGoalScreen
import com.example.expensemanager.ui.history.HistoryScreen
import com.example.expensemanager.ui.scheduled.AddScheduledTransactionScreen
import com.example.expensemanager.ui.scheduled.ScheduledScreen
import com.example.expensemanager.ui.transaction.TransactionsScreen
import com.example.expensemanager.ui.transaction.action.AddExpenseScreen
import com.example.expensemanager.ui.transaction.action.AddIncomeScreen
import com.example.expensemanager.ui.transaction.action.EditTransactionScreen
import com.example.expensemanager.ui.transaction.screen.ExpenseScreen
import com.example.expensemanager.ui.transaction.screen.IncomeScreen
import com.example.expensemanager.viewmodel.ScheduledTransactionViewModel
import com.example.expensemanager.viewmodel.ScheduledTransactionViewModelFactory
import com.example.expensemanager.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import com.example.expensemanager.ui.settings.CurrencySettingScreen
import com.example.expensemanager.utils.CurrencyDisplayConfig
import com.example.expensemanager.ui.settings.SettingsScreen

@Composable
fun AppNavGraph(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    monthlyBudget: Double,
    onMonthlyBudgetChange: (Double) -> Unit,
    savingGoalName: String,
    savingGoalTargetAmount: Double,
    onSavingGoalChange: (String, Double) -> Unit,
    onClearSavingGoal: () -> Unit,
    notifiedBadgeIds: Set<String>,
    onBadgeNotified: (String) -> Unit,
    selectedCurrencyCode: String,
    exchangeRate: Double,
    onCurrencySettingChange: (String, Double) -> Unit
) {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(
        selectedCurrencyCode,
        exchangeRate
    ) {
        CurrencyDisplayConfig.update(
            currencyCode = selectedCurrencyCode,
            rate = exchangeRate
        )
    }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val transactionRepository = TransactionRepository(
        db.transactionDao()
    )

    val transactionViewModel: TransactionViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(transactionRepository) as T
            }
        }
    )

    val scheduledRepository = ScheduledTransactionRepository(
        db.scheduledTransactionDao()
    )

    val scheduledTransactionViewModel: ScheduledTransactionViewModel = viewModel(
        factory = ScheduledTransactionViewModelFactory(scheduledRepository)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                navController = navController,
                drawerState = drawerState
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    scheduledTransactionViewModel = scheduledTransactionViewModel,
                    monthlyBudget = monthlyBudget,
                    savingGoalName = savingGoalName,
                    savingGoalTargetAmount = savingGoalTargetAmount,
                    notifiedBadgeIds = notifiedBadgeIds,
                    onBadgeNotified = onBadgeNotified,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            composable(Screen.Transactions.route) {
                TransactionsScreen(
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            composable(Screen.Scheduled.route) {
                ScheduledScreen(
                    navController = navController,
                    scheduledTransactionViewModel = scheduledTransactionViewModel,
                    transactionViewModel = transactionViewModel,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            composable(Screen.Analytics.route) {
                AnalyticsScreen(
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    monthlyBudget = monthlyBudget,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            composable(Screen.Goals.route) {
                GoalsScreen(
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    monthlyBudget = monthlyBudget,
                    savingGoalName = savingGoalName,
                    savingGoalTargetAmount = savingGoalTargetAmount,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            composable(Screen.AddScheduled.route) {
                AddScheduledTransactionScreen(
                    navController = navController,
                    scheduledTransactionViewModel = scheduledTransactionViewModel
                )
            }

            composable(Screen.AddIncome.route) {
                AddIncomeScreen(
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

            composable(
                route = Screen.EditTransaction.route,
                arguments = listOf(
                    navArgument("transactionId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val transactionId =
                    backStackEntry.arguments?.getInt("transactionId") ?: return@composable

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

            composable(Screen.SetSavingGoal.route) {
                SetSavingGoalScreen(
                    navController = navController,
                    currentGoalName = savingGoalName,
                    currentTargetAmount = savingGoalTargetAmount,
                    onSaveGoal = onSavingGoalChange,
                    onClearGoal = onClearSavingGoal
                )
            }

            composable(Screen.CurrencySetting.route) {
                CurrencySettingScreen(
                    navController = navController,
                    currentCurrencyCode = selectedCurrencyCode,
                    currentExchangeRate = exchangeRate,
                    onSaveCurrencySetting = onCurrencySettingChange
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange,
                    selectedCurrencyCode = selectedCurrencyCode,
                    exchangeRate = exchangeRate,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        }
    }
}