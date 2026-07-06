package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.domain.model.TrainingQuestion
import com.alonso.dotdash.domain.model.TrainingSource

interface TrainingRepository {
    suspend fun loadTraining(source: TrainingSource): MutableList<TrainingQuestion>
    suspend fun restartTraining(source: TrainingSource)
    suspend fun selectAnswer(answer: String)
    suspend fun checkAnswer(): Boolean
    suspend fun nextQuestion(): TrainingQuestion
    suspend fun hasNextQuestion(): Boolean
    suspend fun endTraining()
}

