package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.data.local.TrainingQuestion

interface TrainingRepository {
    suspend fun loadTraining(): MutableList<TrainingQuestion>
    suspend fun selectAnswer(answer: String)
    suspend fun checkAnswer(): Boolean
    suspend fun nextQuestion(): TrainingQuestion
    suspend fun restartTraining()
    suspend fun endTraining()
}

