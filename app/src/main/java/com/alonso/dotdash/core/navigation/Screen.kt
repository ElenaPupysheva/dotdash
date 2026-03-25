package com.alonso.dotdash.core.navigation

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object DictionaryScreen: Screen("dictionary_screen")
    object SettingsScreen: Screen("settings_screen")
    object StatisticScreen: Screen("statistic_screen")
    object TrainingScreen: Screen("training_screen")
}