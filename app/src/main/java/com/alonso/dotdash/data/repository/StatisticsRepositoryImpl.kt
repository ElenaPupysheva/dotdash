package com.alonso.dotdash.data.repository

import com.alonso.dotdash.data.local.StatisticsDataStore
import com.alonso.dotdash.domain.model.Statistics
import com.alonso.dotdash.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow

class StatisticsRepositoryImpl(
    private val dataStore: StatisticsDataStore
) : StatisticsRepository {

    override suspend fun updateStatistics(
        correctAnswers: Int,
        answeredQuestions: Int,
        trainingTimeMillis: Long,
        correctSymbols: Set<String>
    ) {
        dataStore.updateStatistics(
            correctAnswers = correctAnswers,
            answeredQuestions = answeredQuestions,
            trainingTimeMillis = trainingTimeMillis,
            correctSymbols = correctSymbols
        )
    }

    override fun getStatistics(): Flow<Statistics> {
        return dataStore.statisticsFlow
    }

    override suspend fun updateDailyGoal(goal: Int) {
        dataStore.updateDailyGoal(goal)
    }
}
