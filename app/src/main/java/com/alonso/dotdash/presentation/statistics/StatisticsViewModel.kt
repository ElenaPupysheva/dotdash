package com.alonso.dotdash.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonso.dotdash.domain.model.Statistics
import com.alonso.dotdash.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

const val TIMEOUT = 5000L
class StatisticsViewModel(
    repository: StatisticsRepository
) : ViewModel() {
    val statistics = repository.getStatistics().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT),
        initialValue = Statistics(
            totalTrainingsCount = 0,
            totalCorrectAnswers = 0,
            totalAnsweredQuestions = 0
        )
    )
}
