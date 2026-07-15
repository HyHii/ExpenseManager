package com.example.expensemanager.data.local

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.expensemanager.utils.CurrencyCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.currencyDataStore by preferencesDataStore(name = "currency_settings")

class CurrencyDataStore(
    private val context: Context
) {
    companion object {
        private val CURRENCY_CODE_KEY = stringPreferencesKey("currency_code")
        private val EXCHANGE_RATE_KEY = doublePreferencesKey("exchange_rate")
    }

    val selectedCurrencyCode: Flow<String> = context.currencyDataStore.data
        .map { preferences ->
            preferences[CURRENCY_CODE_KEY] ?: CurrencyCode.VND
        }

    val exchangeRate: Flow<Double> = context.currencyDataStore.data
        .map { preferences ->
            preferences[EXCHANGE_RATE_KEY] ?: 1.0
        }

    suspend fun setCurrencySettings(
        currencyCode: String,
        exchangeRate: Double
    ) {
        context.currencyDataStore.edit { preferences ->
            preferences[CURRENCY_CODE_KEY] = currencyCode
            preferences[EXCHANGE_RATE_KEY] = exchangeRate
        }
    }
}