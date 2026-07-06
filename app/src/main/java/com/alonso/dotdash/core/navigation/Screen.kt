package com.alonso.dotdash.core.navigation

import com.alonso.dotdash.domain.model.MorseAlphabet

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object DictionaryScreen: Screen("dictionary_screen")
    object SettingsScreen: Screen("settings_screen")
    object StatisticScreen: Screen("statistic_screen")
    object TrainingScreen : Screen("training_screen/{alphabet}") {
        const val ARG_ALPHABET = "alphabet"

        fun createRoute(alphabet: MorseAlphabet): String {
            return "training_screen/${alphabet.name}"
        }
    }

    object BufferScreen : Screen("buffer_screen")
    object QCodeTrainingScreen : Screen("q_code_training_screen")
}
