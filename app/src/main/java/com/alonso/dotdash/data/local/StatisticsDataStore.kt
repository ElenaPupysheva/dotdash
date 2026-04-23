package com.alonso.dotdash.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alonso.dotdash.domain.model.Statistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val TOTAL_TRAINING_COUNTER = intPreferencesKey("totalTrainingsCount")
val TOTAL_CORRECT_COUNTER = intPreferencesKey("totalCorrectAnswers")
val TOTAL_ANSWERED_COUNTER = intPreferencesKey("totalAnsweredQuestions")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "statistics")

class StatisticsDataStore(
    private val context: Context
) {
    val statisticsFlow: Flow<Statistics> = context.dataStore.data.map { preferences ->
        Statistics(
            totalTrainingsCount = preferences[TOTAL_TRAINING_COUNTER] ?: 0,
            totalCorrectAnswers = preferences[TOTAL_CORRECT_COUNTER] ?: 0,
            totalAnsweredQuestions = preferences[TOTAL_ANSWERED_COUNTER] ?: 0
        )
    }

    suspend fun updateStatistics(
        correctAnswers: Int,
        answeredQuestions: Int
    ) {
        context.dataStore.edit { preferences ->
            val currentTrainings = preferences[TOTAL_TRAINING_COUNTER] ?: 0
            val currentCorrect = preferences[TOTAL_CORRECT_COUNTER] ?: 0
            val currentAnswered = preferences[TOTAL_ANSWERED_COUNTER] ?: 0

            preferences[TOTAL_TRAINING_COUNTER] = currentTrainings + 1
            preferences[TOTAL_CORRECT_COUNTER] = currentCorrect + correctAnswers
            preferences[TOTAL_ANSWERED_COUNTER] = currentAnswered + answeredQuestions
        }
    }
}
