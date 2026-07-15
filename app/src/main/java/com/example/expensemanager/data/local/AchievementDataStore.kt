package com.example.expensemanager.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.achievementDataStore by preferencesDataStore(name = "achievement_settings")

class AchievementDataStore(
    private val context: Context
) {
    companion object {
        private val NOTIFIED_BADGES_KEY = stringSetPreferencesKey("notified_badges")
    }

    val notifiedBadgeIds: Flow<Set<String>> = context.achievementDataStore.data
        .map { preferences ->
            preferences[NOTIFIED_BADGES_KEY] ?: emptySet()
        }

    suspend fun markBadgeAsNotified(badgeId: String) {
        context.achievementDataStore.edit { preferences ->
            val current = preferences[NOTIFIED_BADGES_KEY] ?: emptySet()
            preferences[NOTIFIED_BADGES_KEY] = current + badgeId
        }
    }
}