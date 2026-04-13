package com.alonso.dotdash.domain.usecase

import com.alonso.dotdash.data.local.LocalMorseDataSource
import com.alonso.dotdash.domain.model.TrainingQuestion

const val QUIZSIZE = 10
const val WRONGSIZE = 3
fun createQuizQuestions(): MutableList<TrainingQuestion> {
    val questions = mutableListOf<TrainingQuestion>()
    while (questions.size < QUIZSIZE) {
        val currentSymbol = LocalMorseDataSource.russianSymbols.random()
        val morseCode = currentSymbol.morseCode
        val correctAnswer = currentSymbol.symbol
        val wrongAnswers = mutableListOf<String>()

        while (wrongAnswers.size < WRONGSIZE) {
            val randomSymbol = LocalMorseDataSource.russianSymbols.random()
            val wrongAnswer = randomSymbol.symbol
            if (wrongAnswer != correctAnswer && wrongAnswer !in wrongAnswers) {
                wrongAnswers.add(wrongAnswer)
            }
        }

        val allOptions = (wrongAnswers + correctAnswer).shuffled()
        val question = TrainingQuestion(
            morseCode = morseCode,
            correctAnswer = correctAnswer,
            options = allOptions
        )
        if (question !in questions) {
            questions.add(question)
        }
    }

    return questions
}
