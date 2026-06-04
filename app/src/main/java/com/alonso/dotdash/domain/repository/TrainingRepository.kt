package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.domain.model.TrainingQuestion

interface TrainingRepository {
    suspend fun loadTraining(): MutableList<TrainingQuestion>
    suspend fun selectAnswer(answer: String)
    suspend fun checkAnswer(): Boolean
    suspend fun nextQuestion(): TrainingQuestion
    suspend fun hasNextQuestion(): Boolean
    suspend fun restartTraining()
    suspend fun endTraining()
}

