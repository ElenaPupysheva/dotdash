package com.alonso.dotdash.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonso.dotdash.data.local.HintAllowanceDataStore
import com.alonso.dotdash.domain.model.Statistics
import com.alonso.dotdash.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val DAILY_GOAL = 20
private const val DAILY_FREE_HINTS = 3
private const val START_ZERO = 0
private const val MILLIS = 5000L

class HomeViewModel(
    repository: StatisticsRepository,
    private val hintAllowanceDataStore: HintAllowanceDataStore
) : ViewModel() {
    val statistics = repository.getStatistics().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(MILLIS),
        initialValue = Statistics(
            totalTrainingsCount = START_ZERO,
            totalCorrectAnswers = START_ZERO,
            totalAnsweredQuestions = START_ZERO,
            dailyGoal = DAILY_GOAL,
            todayCorrectAnswers = START_ZERO,
            todayTrainingTimeMillis = 0L,
            lastStatsDate = 0L,
            learnedSymbols = emptySet()
        )
    )

    val hintsRemaining = hintAllowanceDataStore.allowanceFlow
        .map { allowance -> allowance.totalRemaining }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(MILLIS),
            initialValue = DAILY_FREE_HINTS
        )

    fun addRewardedHints(amount: Int = DAILY_FREE_HINTS) {
        viewModelScope.launch {
            hintAllowanceDataStore.addRewardedHints(amount)
        }
    }
}
