package com.alonso.dotdash.data.repository

import com.alonso.dotdash.data.local.StatisticsDataStore
import com.alonso.dotdash.domain.model.Statistics
import com.alonso.dotdash.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StatisticsRepositoryImpl(private val dataStore: StatisticsDataStore) : StatisticsRepository {
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
        dataStore.updateStatistics(
            correctAnswers = correctAnswers,
            answeredQuestions = answeredQuestions
        )
    }

    override fun getStatistics(): Flow<Statistics> {
        return dataStore.statisticsFlow
    }
}
