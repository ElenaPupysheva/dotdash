package com.alonso.dotdash.presentation.training

import com.alonso.dotdash.data.local.TrainingQuestion

data class TrainingState(
    val isLoading: Boolean,
    val trainingQuestion: TrainingQuestion?,
    val selectedAnswer: String?,
    val isAnswerChecked: Boolean,
    val isCorrectAnswer: Boolean?,
    val questionIndex: Int = 0,
    val totalQuestions: Int,
    val correctAnswersCount: Int,
    val isEnded: Boolean,
    val error: String?
)
