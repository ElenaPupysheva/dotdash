package com.alonso.dotdash.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.alonso.dotdash.core.common.DEFAULT_TONE_FREQUENCY_HZ
import com.alonso.dotdash.core.common.MAX_TONE_FREQUENCY_HZ
import com.alonso.dotdash.core.common.MIN_TONE_FREQUENCY_HZ
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appSettingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_settings"
)

private val VIBRATION_ENABLED = booleanPreferencesKey("vibrationEnabled")
private val TRAINING_REMINDER_ENABLED = booleanPreferencesKey("trainingReminderEnabled")
private val LAST_OPENED_EPOCH_DAY = longPreferencesKey("lastOpenedEpochDay")
private val TONE_FREQUENCY_HZ = intPreferencesKey("toneFrequencyHz")

data class AppSettings(
    val vibrationEnabled: Boolean = true,
    val trainingReminderEnabled: Boolean = false,
    val lastOpenedEpochDay: Long = 0L,
    val toneFrequencyHz: Int = DEFAULT_TONE_FREQUENCY_HZ
)

class AppSettingsDataStore(
    private val context: Context
) {
    val settingsFlow: Flow<AppSettings> = context.appSettingsDataStore.data.map { preferences ->
        AppSettings(
            vibrationEnabled = preferences[VIBRATION_ENABLED] ?: true,
            trainingReminderEnabled = preferences[TRAINING_REMINDER_ENABLED] ?: false,
            lastOpenedEpochDay = preferences[LAST_OPENED_EPOCH_DAY] ?: 0L,
            toneFrequencyHz = preferences[TONE_FREQUENCY_HZ]
                ?.coerceIn(MIN_TONE_FREQUENCY_HZ, MAX_TONE_FREQUENCY_HZ)
                ?: DEFAULT_TONE_FREQUENCY_HZ
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

    suspend fun setLastOpenedEpochDay(day: Long) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[LAST_OPENED_EPOCH_DAY] = day
        }
    }

    suspend fun setToneFrequencyHz(frequencyHz: Int) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[TONE_FREQUENCY_HZ] = frequencyHz.coerceIn(
                MIN_TONE_FREQUENCY_HZ,
                MAX_TONE_FREQUENCY_HZ
            )
        }
    }
}
