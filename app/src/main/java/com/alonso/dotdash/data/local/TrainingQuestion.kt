package com.alonso.dotdash.data.local

data class TrainingQuestion(
    val morseCode: String,
    val correctAnswer: String,
    val options: List<String>
)
