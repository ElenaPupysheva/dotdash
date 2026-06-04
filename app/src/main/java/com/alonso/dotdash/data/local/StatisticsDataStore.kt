package com.alonso.dotdash.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alonso.dotdash.domain.model.Statistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private const val DEFAULT_DAILY_GOAL = 20
val TOTAL_TRAINING_COUNTER = intPreferencesKey("totalTrainingsCount")
val TOTAL_CORRECT_COUNTER = intPreferencesKey("totalCorrectAnswers")
val TOTAL_ANSWERED_COUNTER = intPreferencesKey("totalAnsweredQuestions")
val DAILY_GOAL = intPreferencesKey("dailyGoal")
val TODAY_CORRECT_ANSWERS = intPreferencesKey("todayCorrectAnswers")
val TODAY_TRAINING_TIME = longPreferencesKey("todayTrainingTimeMillis")
val LAST_STATS_DATE = longPreferencesKey("lastStatsDate")
val LEARNED_SYMBOLS = stringSetPreferencesKey("learnedSymbols")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "statistics")

class StatisticsDataStore(
    private val context: Context
) {
    val statisticsFlow: Flow<Statistics> = context.dataStore.data.map { preferences ->
        Statistics(
            totalTrainingsCount = preferences[TOTAL_TRAINING_COUNTER] ?: 0,
            totalCorrectAnswers = preferences[TOTAL_CORRECT_COUNTER] ?: 0,
            totalAnsweredQuestions = preferences[TOTAL_ANSWERED_COUNTER] ?: 0,
            dailyGoal = preferences[DAILY_GOAL] ?: DEFAULT_DAILY_GOAL,
            todayCorrectAnswers = preferences[TODAY_CORRECT_ANSWERS] ?: 0,
            todayTrainingTimeMillis = preferences[TODAY_TRAINING_TIME] ?: 0L,
            lastStatsDate = preferences[LAST_STATS_DATE] ?: 0L,
            learnedSymbols = preferences[LEARNED_SYMBOLS] ?: emptySet()
        )
    }

    suspend fun updateStatistics(
        correctAnswers: Int,
        answeredQuestions: Int,
        trainingTimeMillis: Long,
        correctSymbols: Set<String>
    ) {
        context.dataStore.edit { preferences ->
            val todayEpochDay = LocalDate.now().toEpochDay()
            val savedEpochDay = preferences[LAST_STATS_DATE] ?: 0L

            val isNewDay = savedEpochDay != todayEpochDay

            val currentTrainings = preferences[TOTAL_TRAINING_COUNTER] ?: 0
            val currentCorrect = preferences[TOTAL_CORRECT_COUNTER] ?: 0
            val currentAnswered = preferences[TOTAL_ANSWERED_COUNTER] ?: 0

            val currentTodayCorrect = if (isNewDay) {
                0
            } else {
                preferences[TODAY_CORRECT_ANSWERS] ?: 0
            }

            val currentTodayTrainingTime = if (isNewDay) {
                0L
            } else {
                preferences[TODAY_TRAINING_TIME] ?: 0L
            }

            val currentLearnedSymbols = preferences[LEARNED_SYMBOLS] ?: emptySet()

            preferences[TOTAL_TRAINING_COUNTER] = currentTrainings + 1
            preferences[TOTAL_CORRECT_COUNTER] = currentCorrect + correctAnswers
            preferences[TOTAL_ANSWERED_COUNTER] = currentAnswered + answeredQuestions

            preferences[TODAY_CORRECT_ANSWERS] = currentTodayCorrect + correctAnswers
            preferences[TODAY_TRAINING_TIME] = currentTodayTrainingTime + trainingTimeMillis
            preferences[LAST_STATS_DATE] = todayEpochDay
            preferences[LEARNED_SYMBOLS] = currentLearnedSymbols + correctSymbols
        }
    }

    suspend fun updateDailyGoal(goal: Int) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_GOAL] = goal
        }
    }
}
