package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.domain.model.Statistics
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    suspend fun updateStatistics(
        correctAnswers: Int,
        answeredQuestions: Int,
        trainingTimeMillis: Long,
        correctSymbols: Set<String>
    )

    fun getStatistics(): Flow<Statistics>

    suspend fun updateDailyGoal(goal: Int)
}
