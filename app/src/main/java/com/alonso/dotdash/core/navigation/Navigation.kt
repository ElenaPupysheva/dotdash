package com.alonso.dotdash.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alonso.dotdash.data.repository.TrainingRepositoryImpl
import com.alonso.dotdash.presentation.dictionary.DictionaryScreen
import com.alonso.dotdash.presentation.home.HomeScreen
import com.alonso.dotdash.presentation.settings.SettingsScreen
import com.alonso.dotdash.presentation.statistics.StatisticScreen
import com.alonso.dotdash.presentation.training.TrainingScreen
import com.alonso.dotdash.presentation.training.TrainingViewModel
import com.alonso.dotdash.presentation.training.TrainingViewModelFactory

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
            StatisticScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.TrainingScreen.route) {
            val repository = remember { TrainingRepositoryImpl() }
            val factory = remember { TrainingViewModelFactory(repository) }
            val trainingViewModel: TrainingViewModel = viewModel(factory = factory)

            TrainingScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = trainingViewModel
            )
        }
    }
}
