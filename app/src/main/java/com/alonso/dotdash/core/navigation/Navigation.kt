package com.alonso.dotdash.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alonso.dotdash.data.local.AppSettingsDataStore
import com.alonso.dotdash.data.local.StatisticsDataStore
import com.alonso.dotdash.data.repository.AppSettingsRepositoryImpl
import com.alonso.dotdash.data.repository.StatisticsRepositoryImpl
import com.alonso.dotdash.data.repository.TrainingRepositoryImpl
import com.alonso.dotdash.domain.model.MorseAlphabet
import com.alonso.dotdash.domain.model.TrainingGameType
import com.alonso.dotdash.domain.model.TrainingSource
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
import com.alonso.dotdash.presentation.training.BufferScreen
import com.alonso.dotdash.presentation.training.TrainingScreen
import com.alonso.dotdash.presentation.training.TrainingViewModel
import com.alonso.dotdash.presentation.training.TrainingViewModelFactory
import com.alonso.dotdash.presentation.training.qcode.QCodeTrainingScreen

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
            val factory = remember {
                SettingsViewModelFactory(
                    statisticsRepository = statisticsRepository,
                    appSettingsRepository = appSettingsRepository
                )
            }
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

        composable(
            route = Screen.TrainingScreen.route,
            arguments = listOf(
                navArgument(Screen.TrainingScreen.ARG_ALPHABET) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val alphabetName = backStackEntry.arguments
                ?.getString(Screen.TrainingScreen.ARG_ALPHABET)

            val alphabet = runCatching {
                MorseAlphabet.valueOf(alphabetName.orEmpty())
            }.getOrDefault(MorseAlphabet.RUS)

            val source = when (alphabet) {
                MorseAlphabet.RUS -> TrainingSource.RUSSIAN
                MorseAlphabet.ENG -> TrainingSource.ENGLISH
                MorseAlphabet.DIGITS -> TrainingSource.DIGITS
            }

            val factory = remember(source) {
                TrainingViewModelFactory(
                    source = source,
                    repository = trainingRepository,
                    statisticsRepository = statisticsRepository,
                    appSettingsRepository = appSettingsRepository
                )
            }

            val trainingViewModel: TrainingViewModel = viewModel(factory = factory)

            TrainingScreen(
                onBackClick = {
                    navController.popBackStack(
                        route = Screen.BufferScreen.route,
                        inclusive = false
                    )
                },
                viewModel = trainingViewModel
            )
        }

        composable(Screen.BufferScreen.route) {
            BufferScreen(
                onBackClick = {
                    navController.popBackStack(
                        route = Screen.HomeScreen.route,
                        inclusive = false
                    )
                },
                onPlayClick = { gameType, alphabet ->
                    when (gameType) {
                        TrainingGameType.CLASSIC -> {
                            alphabet?.let {
                                navController.navigate(
                                    Screen.TrainingScreen.createRoute(it)
                                )
                            }
                        }

                        TrainingGameType.QCODE -> {
                            navController.navigate(Screen.QCodeTrainingScreen.route)
                        }
                    }
                }
            )
        }

        composable(Screen.QCodeTrainingScreen.route) {
            val factory = remember {
                TrainingViewModelFactory(
                    source = TrainingSource.Q_CODES,
                    repository = trainingRepository,
                    statisticsRepository = statisticsRepository,
                    appSettingsRepository = appSettingsRepository
                )
            }

            val qCodeViewModel: TrainingViewModel = viewModel(factory = factory)

            QCodeTrainingScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = qCodeViewModel
            )
        }
    }
}
