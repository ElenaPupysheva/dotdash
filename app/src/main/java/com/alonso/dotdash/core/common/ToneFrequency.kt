package com.alonso.dotdash.core.common

const val MIN_TONE_FREQUENCY_HZ = 300
const val MAX_TONE_FREQUENCY_HZ = 1_000
const val DEFAULT_TONE_FREQUENCY_HZ = 650
const val TONE_FREQUENCY_STEP_HZ = 50

object ToneFrequency {
    @Volatile
    var valueHz: Int = DEFAULT_TONE_FREQUENCY_HZ
        private set

    fun update(frequencyHz: Int) {
        valueHz = frequencyHz.coerceIn(
            MIN_TONE_FREQUENCY_HZ,
            MAX_TONE_FREQUENCY_HZ
        )
    }
}
