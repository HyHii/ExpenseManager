package com.example.expensemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.example.expensemanager.data.local.BudgetDataStore
import com.example.expensemanager.data.local.ThemeDataStore
import com.example.expensemanager.ui.navigation.AppNavGraph
import com.example.expensemanager.ui.theme.ExpenseManagerTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeDataStore = ThemeDataStore(this)
        val budgetDataStore = BudgetDataStore(this)

        setContent {
            val isDarkMode by themeDataStore.isDarkMode.collectAsState(initial = false)
            val monthlyBudget by budgetDataStore.monthlyBudget.collectAsState(initial = 5_000_000.0)

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
                    }
                )
            }
        }
    }
}