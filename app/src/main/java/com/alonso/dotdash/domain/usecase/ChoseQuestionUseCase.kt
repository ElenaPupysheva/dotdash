package com.alonso.dotdash.domain.usecase

import com.alonso.dotdash.data.local.LocalMorseDataSource
import com.alonso.dotdash.domain.model.MorseAlphabet
import com.alonso.dotdash.domain.model.TrainingQuestion

const val QUIZSIZE = 10
const val WRONGSIZE = 3
fun createQuizQuestions(alphabet: MorseAlphabet): MutableList<TrainingQuestion> {
    val sourceSymbols = when (alphabet) {
        MorseAlphabet.RUS -> LocalMorseDataSource.russianSymbols
        MorseAlphabet.ENG -> LocalMorseDataSource.englishSymbols
        MorseAlphabet.DIGITS -> LocalMorseDataSource.digitsSymbols
    }
    val questions = mutableListOf<TrainingQuestion>()
    while (questions.size < QUIZSIZE) {
        val currentSymbol = sourceSymbols.random()
        val morseCode = currentSymbol.morseCode
        val correctAnswer = currentSymbol.symbol
        val wrongAnswers = mutableListOf<String>()

        while (wrongAnswers.size < WRONGSIZE) {
            val randomSymbol = sourceSymbols.random()
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

fun createQCodeQuestions(): MutableList<TrainingQuestion> {
    return LocalMorseDataSource.qcodeSymbols
        .shuffled()
        .take(QUIZSIZE)
        .map { currentSymbol ->
            val wrongAnswers = LocalMorseDataSource.qcodeSymbols
                .filter { it.id != currentSymbol.id }
                .shuffled()
                .take(WRONGSIZE)
                .map { it.symbol }

            TrainingQuestion(
                morseCode = currentSymbol.morseCode,
                correctAnswer = currentSymbol.symbol,
                options = (wrongAnswers + currentSymbol.symbol).shuffled(),
                hint = currentSymbol.meaning?.current()
            )
        }
        .toMutableList()
}

fun createGreetingsQuestions(): MutableList<TrainingQuestion> {
    return LocalMorseDataSource.greetingsSymbols
        .shuffled()
        .take(QUIZSIZE)
        .map { currentSymbol ->
            val wrongAnswers = LocalMorseDataSource.greetingsSymbols
                .filter { it.id != currentSymbol.id }
                .shuffled()
                .take(WRONGSIZE)
                .map { it.symbol }

            TrainingQuestion(
                morseCode = currentSymbol.morseCode,
                correctAnswer = currentSymbol.symbol,
                options = (wrongAnswers + currentSymbol.symbol).shuffled(),
                hint = currentSymbol.meaning?.current()
            )
        }
        .toMutableList()
}

