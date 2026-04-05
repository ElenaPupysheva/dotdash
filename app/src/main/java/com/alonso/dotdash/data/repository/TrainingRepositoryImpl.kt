package com.alonso.dotdash.data.repository

import com.alonso.dotdash.data.local.TrainingQuestion
import com.alonso.dotdash.domain.repository.TrainingRepository
import com.alonso.dotdash.domain.usecase.createQuizQuestions

class TrainingRepositoryImpl : TrainingRepository {
    private var currentQuiz: MutableList<TrainingQuestion> = mutableListOf()
    private var currentQuizIndex: Int = 0
    private var selectedAnswer: String? = null

    override suspend fun loadTraining(): MutableList<TrainingQuestion> {
        currentQuiz = createQuizQuestions()
        return currentQuiz
    }

    override suspend fun selectAnswer(answer: String) {
        selectedAnswer = answer
    }

    override suspend fun checkAnswer(): Boolean {
        val currentQ = currentQuiz[currentQuizIndex]
        val isCorrect = currentQ.correctAnswer == selectedAnswer
        return isCorrect
    }

    override suspend fun nextQuestion(): TrainingQuestion {
        currentQuizIndex++
        selectedAnswer = null
        return currentQuiz[currentQuizIndex]
    }
    override suspend fun hasNextQuestion(): Boolean {
        return currentQuizIndex < currentQuiz.size - 1
    }

    override suspend fun restartTraining() {
        loadTraining()
    }

    override suspend fun endTraining() {
        currentQuiz.clear()
        currentQuizIndex = 0
        selectedAnswer = null
    }
}
