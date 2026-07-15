package com.example.expensemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.example.expensemanager.data.local.AchievementDataStore
import com.example.expensemanager.data.local.BudgetDataStore
import com.example.expensemanager.data.local.CurrencyDataStore
import com.example.expensemanager.data.local.SavingGoalDataStore
import com.example.expensemanager.data.local.ThemeDataStore
import com.example.expensemanager.ui.navigation.AppNavGraph
import com.example.expensemanager.ui.theme.ExpenseManagerTheme
import com.example.expensemanager.utils.CurrencyCode
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeDataStore = ThemeDataStore(this)
        val budgetDataStore = BudgetDataStore(this)
        val savingGoalDataStore = SavingGoalDataStore(this)
        val achievementDataStore = AchievementDataStore(this)
        val currencyDataStore = CurrencyDataStore(this)

        setContent {
            val isDarkMode by themeDataStore.isDarkMode.collectAsState(
                initial = false
            )

            val monthlyBudget by budgetDataStore.monthlyBudget.collectAsState(
                initial = 5_000_000.0
            )

            val savingGoalName by savingGoalDataStore.goalName.collectAsState(
                initial = ""
            )

            val savingGoalTargetAmount by savingGoalDataStore.goalTargetAmount.collectAsState(
                initial = 0.0
            )

            val notifiedBadgeIds by achievementDataStore.notifiedBadgeIds.collectAsState(
                initial = emptySet()
            )

            val selectedCurrencyCode by currencyDataStore.selectedCurrencyCode.collectAsState(
                initial = CurrencyCode.VND
            )

            val exchangeRate by currencyDataStore.exchangeRate.collectAsState(
                initial = 1.0
            )

            ExpenseManagerTheme(
                darkTheme = isDarkMode,
                dynamicColor = false
            ) {
                AppNavGraph(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { checked ->
                        lifecycleScope.launch {
                            themeDataStore.setDarkMode(checked)
                        }
                    },
                    monthlyBudget = monthlyBudget,
                    onMonthlyBudgetChange = { budget ->
                        lifecycleScope.launch {
                            budgetDataStore.setMonthlyBudget(budget)
                        }
                    },
                    savingGoalName = savingGoalName,
                    savingGoalTargetAmount = savingGoalTargetAmount,
                    onSavingGoalChange = { name, targetAmount ->
                        lifecycleScope.launch {
                            savingGoalDataStore.setSavingGoal(
                                name = name,
                                targetAmount = targetAmount
                            )
                        }
                    },
                    onClearSavingGoal = {
                        lifecycleScope.launch {
                            savingGoalDataStore.clearSavingGoal()
                        }
                    },
                    notifiedBadgeIds = notifiedBadgeIds,
                    onBadgeNotified = { badgeId ->
                        lifecycleScope.launch {
                            achievementDataStore.markBadgeAsNotified(badgeId)
                        }
                    },
                    selectedCurrencyCode = selectedCurrencyCode,
                    exchangeRate = exchangeRate,
                    onCurrencySettingChange = { currencyCode, rate ->
                        lifecycleScope.launch {
                            currencyDataStore.setCurrencySettings(
                                currencyCode = currencyCode,
                                exchangeRate = rate
                            )
                        }
                    }
                )
            }
        }
    }
}