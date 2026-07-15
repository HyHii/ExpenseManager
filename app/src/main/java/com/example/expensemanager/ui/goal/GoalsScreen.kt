package com.example.expensemanager.ui.goal

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
import com.example.expensemanager.ui.budget.BudgetProgressCard
import com.example.expensemanager.ui.gamification.AchievementBadgesCard
import com.example.expensemanager.ui.gamification.SavingStreakCard
import com.example.expensemanager.ui.navigation.Screen
import com.example.expensemanager.utils.calculateAchievements
import com.example.expensemanager.utils.calculateSavingStreak
import com.example.expensemanager.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    monthlyBudget: Double,
    savingGoalName: String,
    savingGoalTargetAmount: Double,
    onMenuClick: () -> Unit = {}
) {
    val transactions = transactionViewModel.allTransactions()

    val totalExpense = transactionViewModel.totalExpense()
    val currentBalance = transactionViewModel.currentBalance()

    val savingStreakState = calculateSavingStreak(
        transactions = transactions,
        monthlyBudget = monthlyBudget
    )

    val achievements = calculateAchievements(
        transactions = transactions,
        totalExpense = totalExpense,
        currentBalance = currentBalance,
        monthlyBudget = monthlyBudget,
        savingGoalName = savingGoalName,
        savingGoalTargetAmount = savingGoalTargetAmount,
        savingStreakState = savingStreakState
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
                        text = "Goals",
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
                SavingGoalCard(
                    goalName = savingGoalName,
                    targetAmount = savingGoalTargetAmount,
                    currentBalance = currentBalance,
                    onClick = {
                        navController.navigate(Screen.SetSavingGoal.route)
                    }
                )
            }

            item {
                BudgetProgressCard(
                    monthlyBudget = monthlyBudget,
                    totalExpense = totalExpense,
                    onClick = {
                        navController.navigate(Screen.SetBudget.route)
                    }
                )
            }

            item {
                SavingStreakCard(
                    state = savingStreakState
                )
            }

            item {
                AchievementBadgesCard(
                    badges = achievements
                )
            }
        }
    }
}