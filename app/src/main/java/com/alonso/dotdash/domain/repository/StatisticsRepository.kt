package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.domain.model.Statistics
import kotlinx.coroutines.flow.StateFlow

interface StatisticsRepository {
    suspend fun updateStatistics(correctAnswers: Int, answeredQuestions: Int)
    fun getStatistics(): StateFlow<Statistics>
}
