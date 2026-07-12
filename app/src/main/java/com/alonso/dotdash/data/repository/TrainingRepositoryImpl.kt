package com.alonso.dotdash.data.repository

import com.alonso.dotdash.domain.model.MorseAlphabet
import com.alonso.dotdash.domain.model.TrainingQuestion
import com.alonso.dotdash.domain.model.TrainingSource
import com.alonso.dotdash.domain.repository.TrainingRepository
import com.alonso.dotdash.domain.usecase.createGreetingsQuestions
import com.alonso.dotdash.domain.usecase.createQCodeQuestions
import com.alonso.dotdash.domain.usecase.createQuizQuestions

class TrainingRepositoryImpl : TrainingRepository {
    private var currentQuiz: MutableList<TrainingQuestion> = mutableListOf()
    private var currentQuizIndex: Int = 0
    private var selectedAnswer: String? = null

    override suspend fun loadTraining(
        source: TrainingSource
    ): MutableList<TrainingQuestion> {
        currentQuizIndex = 0
        selectedAnswer = null

        currentQuiz = when (source) {
            TrainingSource.RUSSIAN ->
                createQuizQuestions(MorseAlphabet.RUS)

            TrainingSource.ENGLISH ->
                createQuizQuestions(MorseAlphabet.ENG)

            TrainingSource.DIGITS ->
                createQuizQuestions(MorseAlphabet.DIGITS)

            TrainingSource.Q_CODES ->
                createQCodeQuestions()

            TrainingSource.GREETINGS ->
                createGreetingsQuestions()
        }

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

    override suspend fun restartTraining(source: TrainingSource) {
        loadTraining(source)
    }

    override suspend fun endTraining() {
        currentQuiz.clear()
        currentQuizIndex = 0
        selectedAnswer = null
    }
}
