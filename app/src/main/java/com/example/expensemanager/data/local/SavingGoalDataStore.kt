package com.example.expensemanager.data.local

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.savingGoalDataStore by preferencesDataStore(name = "saving_goal_settings")

class SavingGoalDataStore(
    private val context: Context
) {
    companion object {
        private val GOAL_NAME_KEY = stringPreferencesKey("goal_name")
        private val GOAL_TARGET_KEY = doublePreferencesKey("goal_target")
    }

    val goalName: Flow<String> = context.savingGoalDataStore.data
        .map { preferences ->
            preferences[GOAL_NAME_KEY] ?: ""
        }

    val goalTargetAmount: Flow<Double> = context.savingGoalDataStore.data
        .map { preferences ->
            preferences[GOAL_TARGET_KEY] ?: 0.0
        }

    suspend fun setSavingGoal(name: String, targetAmount: Double) {
        context.savingGoalDataStore.edit { preferences ->
            preferences[GOAL_NAME_KEY] = name
            preferences[GOAL_TARGET_KEY] = targetAmount
        }
    }

    suspend fun clearSavingGoal() {
        context.savingGoalDataStore.edit { preferences ->
            preferences[GOAL_NAME_KEY] = ""
            preferences[GOAL_TARGET_KEY] = 0.0
        }
    }
}