package com.alonso.dotdash.domain.model

private const val NO_ANSWERED_QUESTIONS = 0
private const val PERCENT_BASE = 100
private const val DEFAULT_DAILY_GOAL = 20
private const val MILLIS_IN_MINUTE = 60_000L
data class Statistics(
    val totalTrainingsCount: Int,
    val totalCorrectAnswers: Int,
    val totalAnsweredQuestions: Int,
    val dailyGoal: Int = DEFAULT_DAILY_GOAL,
    val todayCorrectAnswers: Int = 0,
    val todayTrainingTimeMillis: Long = 0L,
    val lastStatsDate: Long = 0L,
    val learnedSymbols: Set<String> = emptySet()
) {
    val accuracyPercent: Int
        get() = if (totalAnsweredQuestions == NO_ANSWERED_QUESTIONS) {
            NO_ANSWERED_QUESTIONS
        } else {
            totalCorrectAnswers * PERCENT_BASE / totalAnsweredQuestions
        }

    val learnedSymbolsCount: Int
        get() = learnedSymbols.size

    val todayTrainingMinutes: Long
        get() = todayTrainingTimeMillis / MILLIS_IN_MINUTE
}
