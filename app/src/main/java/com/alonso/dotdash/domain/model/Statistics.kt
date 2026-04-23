package com.alonso.dotdash.domain.model

private const val NO_ANSWERED_QUESTIONS = 0
private const val PERCENT_BASE = 100

data class Statistics(
    val totalTrainingsCount: Int,
    val totalCorrectAnswers: Int,
    val totalAnsweredQuestions: Int
) {
    val accuracyPercent: Int
        get() = if (totalAnsweredQuestions == NO_ANSWERED_QUESTIONS) {
            NO_ANSWERED_QUESTIONS
        } else {
            totalCorrectAnswers * PERCENT_BASE / totalAnsweredQuestions
        }
}
