package com.alonso.dotdash.data

import com.alonso.dotdash.data.local.TrainingQuestion
import com.alonso.dotdash.domain.repository.TrainingRepository

class TrainingRepositoryImpl : TrainingRepository {
    override suspend fun loadTraining(): TrainingQuestion {
        TODO("Not yet implemented")
    }

    override suspend fun selectAnswer() {
        TODO("Not yet implemented")
    }

    override suspend fun checkAnswer() {
        TODO("Not yet implemented")
    }

    override suspend fun nextQuestion(): TrainingQuestion {
        TODO("Not yet implemented")
    }

    override suspend fun restartTraining() {
        TODO("Not yet implemented")
    }

    override suspend fun endTraining() {
        TODO("Not yet implemented")
    }
}
