package com.alonso.dotdash.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alonso.dotdash.data.local.AppSettingsDataStore
import com.alonso.dotdash.data.local.StatisticsDataStore
import com.alonso.dotdash.data.repository.AppSettingsRepositoryImpl
import com.alonso.dotdash.data.repository.StatisticsRepositoryImpl
import com.alonso.dotdash.data.repository.TrainingRepositoryImpl
import com.alonso.dotdash.presentation.dictionary.DictionaryScreen
import com.alonso.dotdash.presentation.home.HomeScreen
import com.alonso.dotdash.presentation.home.HomeViewModel
import com.alonso.dotdash.presentation.home.HomeViewModelFactory
import com.alonso.dotdash.presentation.settings.SettingsScreen
import com.alonso.dotdash.presentation.settings.SettingsViewModel
import com.alonso.dotdash.presentation.settings.SettingsViewModelFactory
import com.alonso.dotdash.presentation.statistics.StatisticScreen
import com.alonso.dotdash.presentation.statistics.StatisticsViewModel
import com.alonso.dotdash.presentation.statistics.StatisticsViewModelFactory
import com.alonso.dotdash.presentation.training.TrainingScreen
import com.alonso.dotdash.presentation.training.TrainingViewModel
import com.alonso.dotdash.presentation.training.TrainingViewModelFactory

@Composable
fun Navigation(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val appContext = LocalContext.current.applicationContext

    val statisticsDataStore = remember(appContext) {
        StatisticsDataStore(appContext)
    }
    val statisticsRepository = remember(statisticsDataStore) {
        StatisticsRepositoryImpl(statisticsDataStore)
    }
    val trainingRepository = remember { TrainingRepositoryImpl() }

    val appSettingsDataStore = remember(appContext) {
        AppSettingsDataStore(appContext)
    }
    val appSettingsRepository = remember(appSettingsDataStore) {
        AppSettingsRepositoryImpl(appSettingsDataStore)
    }

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            val factory = remember { HomeViewModelFactory(statisticsRepository) }
            val homeViewModel: HomeViewModel = viewModel(factory = factory)

            HomeScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }

        composable(Screen.DictionaryScreen.route) {
            DictionaryScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.SettingsScreen.route) {
            val factory = remember { SettingsViewModelFactory(statisticsRepository) }
            val settingsViewModel: SettingsViewModel = viewModel(factory = factory)

            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                viewModel = settingsViewModel
            )
        }

        composable(Screen.StatisticScreen.route) {
            val factory = remember { StatisticsViewModelFactory(statisticsRepository) }
            val statisticsViewModel: StatisticsViewModel = viewModel(factory = factory)

            StatisticScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = statisticsViewModel
            )
        }

        composable(Screen.TrainingScreen.route) {
            val factory = remember {
                TrainingViewModelFactory(
                    repository = trainingRepository,
                    statisticsRepository = statisticsRepository
                )
            }
            val trainingViewModel: TrainingViewModel = viewModel(factory = factory)

            TrainingScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = trainingViewModel
            )
        }
    }
}
