package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.data.local.TrainingQuestion

interface TrainingRepository {
    suspend fun loadTraining(): TrainingQuestion
    suspend fun selectAnswer()
    suspend fun checkAnswer()
    suspend fun nextQuestion(): TrainingQuestion
    suspend fun restartTraining()
    suspend fun endTraining()
}

