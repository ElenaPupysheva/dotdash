package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.domain.model.Statistics
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    suspend fun updateStatistics(correctAnswers: Int, answeredQuestions: Int)
    fun getStatistics(): Flow<Statistics>
}
