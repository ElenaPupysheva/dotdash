package com.alonso.dotdash.domain.model

enum class TrainingGameType {
    CLASSIC,
    QCODE,
    GREETINGS,
    FREE_WRITING
}

enum class TrainingDifficulty { NORMAL, HARD }
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
            ),
            TrainingTypes(
                type = TrainingGameType.GREETINGS,
                alphabets = emptyList()
            ),
            TrainingTypes(
                type = TrainingGameType.FREE_WRITING,
                alphabets = emptyList()
            )
        )
    }
}
