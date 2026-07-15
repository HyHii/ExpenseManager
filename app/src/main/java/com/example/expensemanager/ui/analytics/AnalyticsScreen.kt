package com.example.expensemanager.ui.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanager.ui.analytics.chart.BalanceTrendChartCard
import com.example.expensemanager.ui.analytics.chart.IncomeExpenseChartCard
import com.example.expensemanager.ui.analytics.chart.SpendingTrendChartCard
import com.example.expensemanager.utils.calculateDailyBalanceChartData
import com.example.expensemanager.utils.calculateDailyIncomeExpenseChartData
import com.example.expensemanager.utils.calculateDailySpendingChartData
import com.example.expensemanager.utils.calculateExpenseAnalytics
import com.example.expensemanager.utils.calculateMonthlyReport
import com.example.expensemanager.utils.calculateSpendingTrend
import com.example.expensemanager.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    monthlyBudget: Double,
    onMenuClick: () -> Unit = {}
) {
    val transactions = transactionViewModel.allTransactions()

    val monthlyReport = calculateMonthlyReport(
        transactions = transactions,
        monthlyBudget = monthlyBudget
    )

    val expenseCategorySummaries = calculateExpenseAnalytics(
        transactions = transactions
    )

    val spendingTrend = calculateSpendingTrend(
        transactions = transactions
    )

    val spendingChartPoints = calculateDailySpendingChartData(
        transactions = transactions
    )

    val incomeExpenseChartPoints = calculateDailyIncomeExpenseChartData(
        transactions = transactions
    )

    val balanceChartPoints = calculateDailyBalanceChartData(
        transactions = transactions
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open menu"
                        )
                    }
                },
                title = {
                    Text(
                        text = "Analytics",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MonthlyReportCard(
                    report = monthlyReport
                )
            }

            item {
                ExpenseAnalyticsCard(
                    categorySummaries = expenseCategorySummaries
                )
            }

            item {
                SpendingTrendCard(
                    trend = spendingTrend
                )
            }

            item {
                SpendingTrendChartCard(
                    points = spendingChartPoints
                )
            }

            item {
                IncomeExpenseChartCard(
                    points = incomeExpenseChartPoints
                )
            }

            item {
                BalanceTrendChartCard(
                    points = balanceChartPoints
                )
            }
        }
    }
}