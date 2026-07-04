package com.example.expensemanager.data.local

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.budgetDataStore by preferencesDataStore(name = "budget_settings")

class BudgetDataStore(
    private val context: Context
) {
    companion object {
        private val MONTHLY_BUDGET_KEY = doublePreferencesKey("monthly_budget")
    }

    val monthlyBudget: Flow<Double> = context.budgetDataStore.data
        .map { preferences ->
            preferences[MONTHLY_BUDGET_KEY] ?: 5_000_000.0
        }

    suspend fun setMonthlyBudget(budget: Double) {
        context.budgetDataStore.edit { preferences ->
            preferences[MONTHLY_BUDGET_KEY] = budget
        }
    }
}