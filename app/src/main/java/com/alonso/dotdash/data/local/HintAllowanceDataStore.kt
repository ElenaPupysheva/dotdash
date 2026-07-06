package com.alonso.dotdash.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private const val DAILY_FREE_HINTS = 3

private val Context.hintAllowanceDataStore by preferencesDataStore(
    name = "hint_allowance"
)

private val HINT_DATE = longPreferencesKey("hintDate")
private val FREE_HINTS_USED = intPreferencesKey("freeHintsUsed")
private val REWARDED_HINTS = intPreferencesKey("rewardedHints")

data class HintAllowance(
    val freeHintsRemaining: Int = DAILY_FREE_HINTS,
    val rewardedHints: Int = 0
) {
    val totalRemaining: Int
        get() = freeHintsRemaining + rewardedHints
}

class HintAllowanceDataStore(
    private val context: Context
) {
    val allowanceFlow: Flow<HintAllowance> =
        context.hintAllowanceDataStore.data.map { preferences ->
            val today = LocalDate.now().toEpochDay()
            val savedDate = preferences[HINT_DATE]

            if (savedDate != today) {
                HintAllowance()
            } else {
                HintAllowance(
                    freeHintsRemaining = (
                            DAILY_FREE_HINTS -
                                    (preferences[FREE_HINTS_USED] ?: 0)
                            ).coerceAtLeast(0),
                    rewardedHints = preferences[REWARDED_HINTS] ?: 0
                )
            }
        }

    suspend fun consumeHint(): Boolean {
        var consumed = false

        context.hintAllowanceDataStore.edit { preferences ->
            val today = LocalDate.now().toEpochDay()

            if (preferences[HINT_DATE] != today) {
                preferences[HINT_DATE] = today
                preferences[FREE_HINTS_USED] = 0
                preferences[REWARDED_HINTS] = 0
            }

            val freeUsed = preferences[FREE_HINTS_USED] ?: 0
            val rewarded = preferences[REWARDED_HINTS] ?: 0

            when {
                freeUsed < DAILY_FREE_HINTS -> {
                    preferences[FREE_HINTS_USED] = freeUsed + 1
                    consumed = true
                }

                rewarded > 0 -> {
                    preferences[REWARDED_HINTS] = rewarded - 1
                    consumed = true
                }
            }
        }

        return consumed
    }

    suspend fun addRewardedHints(amount: Int = 3) {
        context.hintAllowanceDataStore.edit { preferences ->
            val current = preferences[REWARDED_HINTS] ?: 0
            preferences[REWARDED_HINTS] = current + amount
        }
    }
}
