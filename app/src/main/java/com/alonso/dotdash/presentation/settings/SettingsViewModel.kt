package com.alonso.dotdash.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonso.dotdash.data.local.AppSettings
import com.alonso.dotdash.domain.model.Statistics
import com.alonso.dotdash.domain.repository.AppSettingsRepository
import com.alonso.dotdash.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val DEFAULT_DAILY_GOAL = 20
const val MIN_DAILY_GOAL = 1
const val MAX_DAILY_GOAL = 100
private const val MILLIS = 5000L

class SettingsViewModel(
    private val statisticsRepository: StatisticsRepository,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    val statistics = statisticsRepository.getStatistics().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(MILLIS),
        initialValue = Statistics(
            totalTrainingsCount = 0,
            totalCorrectAnswers = 0,
            totalAnsweredQuestions = 0,
            dailyGoal = DEFAULT_DAILY_GOAL,
            todayCorrectAnswers = 0,
            todayTrainingTimeMillis = 0L,
            lastStatsDate = 0L,
            learnedSymbols = emptySet()
        )
    )

    val appSettings = appSettingsRepository.getSettings().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(MILLIS),
        initialValue = AppSettings()
    )

    fun updateDailyGoal(goal: Int) {
        val normalizedGoal = goal.coerceIn(MIN_DAILY_GOAL, MAX_DAILY_GOAL)

        viewModelScope.launch {
            statisticsRepository.updateDailyGoal(normalizedGoal)
        }
    }

    fun updateVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.setVibrationEnabled(enabled)
        }
    }

    fun updateTrainingReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.setTrainingReminderEnabled(enabled)
        }
    }
}
