package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.domain.model.MorseAlphabet
import com.alonso.dotdash.domain.model.TrainingQuestion

interface TrainingRepository {
    suspend fun loadTraining(alphabet: MorseAlphabet): MutableList<TrainingQuestion>
    suspend fun restartTraining(alphabet: MorseAlphabet)
    suspend fun selectAnswer(answer: String)
    suspend fun checkAnswer(): Boolean
    suspend fun nextQuestion(): TrainingQuestion
    suspend fun hasNextQuestion(): Boolean
    suspend fun endTraining()
}

