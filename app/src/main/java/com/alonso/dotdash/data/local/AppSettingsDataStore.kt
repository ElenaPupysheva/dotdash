package com.alonso.dotdash.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appSettingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_settings"
)

private val VIBRATION_ENABLED = booleanPreferencesKey("vibrationEnabled")
private val TRAINING_REMINDER_ENABLED = booleanPreferencesKey("trainingReminderEnabled")

data class AppSettings(
    val vibrationEnabled: Boolean = true,
    val trainingReminderEnabled: Boolean = false
)

class AppSettingsDataStore(
    private val context: Context
) {
    val settingsFlow: Flow<AppSettings> = context.appSettingsDataStore.data.map { preferences ->
        AppSettings(
            vibrationEnabled = preferences[VIBRATION_ENABLED] ?: true,
            trainingReminderEnabled = preferences[TRAINING_REMINDER_ENABLED] ?: false
        )
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[VIBRATION_ENABLED] = enabled
        }
    }

    suspend fun setTrainingReminderEnabled(enabled: Boolean) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[TRAINING_REMINDER_ENABLED] = enabled
        }
    }
}
