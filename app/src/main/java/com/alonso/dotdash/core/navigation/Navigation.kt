package com.alonso.dotdash.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alonso.dotdash.data.local.TrainingQuestion
import com.alonso.dotdash.presentation.dictionary.DictionaryScreen
import com.alonso.dotdash.presentation.home.HomeScreen
import com.alonso.dotdash.presentation.settings.SettingsScreen
import com.alonso.dotdash.presentation.statistics.StatisticScreen
import com.alonso.dotdash.presentation.training.TrainingScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.DictionaryScreen.route) {
            DictionaryScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.SettingsScreen.route) {
            SettingsScreen()
        }

        composable(Screen.StatisticScreen.route) {
            StatisticScreen()
        }

        composable(Screen.TrainingScreen.route) {
            TrainingScreen(
                onBackClick = { navController.popBackStack() },
                question = TrainingQuestion(
                    morseCode = ".-",
                    correctAnswer = "A",
                    options = listOf("A", "N", "M", "R")
                ),
            )
        }
    }
}
