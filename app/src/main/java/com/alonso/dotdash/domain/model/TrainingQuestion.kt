package com.alonso.dotdash.domain.model

data class TrainingQuestion(
    val morseCode: String,
    val correctAnswer: String,
    val options: List<String>
)
