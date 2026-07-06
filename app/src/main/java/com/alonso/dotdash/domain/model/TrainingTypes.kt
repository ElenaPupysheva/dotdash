package com.alonso.dotdash.domain.model

enum class TrainingGameType {
    CLASSIC,
    QCODE
}

data class TrainingTypes(
    val type: TrainingGameType,
    val alphabets: List<MorseAlphabet>
) {
    companion object {
        val games = listOf(
            TrainingTypes(
                type = TrainingGameType.CLASSIC,
                alphabets = MorseAlphabet.entries
            ),
            TrainingTypes(
                type = TrainingGameType.QCODE,
                alphabets = emptyList()
            )
        )
    }
}
