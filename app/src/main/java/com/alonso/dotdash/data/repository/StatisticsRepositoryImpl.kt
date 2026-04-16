package com.alonso.dotdash.data.repository

import com.alonso.dotdash.domain.model.Statistics
import com.alonso.dotdash.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StatisticsRepositoryImpl : StatisticsRepository {
    private val statistics = MutableStateFlow(
        Statistics(
            totalTrainingsCount = 0,
            totalCorrectAnswers = 0,
            totalAnsweredQuestions = 0
        )
    )

    override suspend fun updateStatistics(
        correctAnswers: Int,
        answeredQuestions: Int
    ) {
        statistics.value = statistics.value.copy(
            totalTrainingsCount = statistics.value.totalTrainingsCount + 1,
            totalCorrectAnswers = statistics.value.totalCorrectAnswers + correctAnswers,
            totalAnsweredQuestions = statistics.value.totalAnsweredQuestions + answeredQuestions
        )
    }

    override fun getStatistics(): StateFlow<Statistics> {
        return statistics.asStateFlow()
    }
}
