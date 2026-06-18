package com.alonso.dotdash

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.alonso.dotdash.core.navigation.Navigation
import com.alonso.dotdash.data.local.AppSettingsDataStore
import com.alonso.dotdash.data.repository.AppSettingsRepositoryImpl
import com.alonso.dotdash.ui.theme.DotdashTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private val appSettingsRepository by lazy {
        AppSettingsRepositoryImpl(
            AppSettingsDataStore(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            val view = LocalView.current

            SideEffect {
                val window = (view.context as Activity).window

                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    !isDarkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                    !isDarkTheme
            }

            DotdashTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false
            ) {
                Navigation(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = it }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            val today = LocalDate.now().toEpochDay()
            appSettingsRepository.setLastOpenedEpochDay(today)
        }
    }
}