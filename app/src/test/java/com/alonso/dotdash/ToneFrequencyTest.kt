package com.alonso.dotdash

import com.alonso.dotdash.core.common.DEFAULT_TONE_FREQUENCY_HZ
import com.alonso.dotdash.core.common.MAX_TONE_FREQUENCY_HZ
import com.alonso.dotdash.core.common.MIN_TONE_FREQUENCY_HZ
import com.alonso.dotdash.core.common.TONE_FREQUENCY_STEP_HZ
import com.alonso.dotdash.core.common.ToneFrequency
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class ToneFrequencyTest {
    @After
    fun resetFrequency() {
        ToneFrequency.update(DEFAULT_TONE_FREQUENCY_HZ)
    }

    @Test
    fun `update stores frequency within supported range`() {
        ToneFrequency.update(800)

        assertEquals(800, ToneFrequency.valueHz)
    }

    @Test
    fun `update clamps frequency below supported range`() {
        ToneFrequency.update(MIN_TONE_FREQUENCY_HZ - TONE_FREQUENCY_STEP_HZ)

        assertEquals(MIN_TONE_FREQUENCY_HZ, ToneFrequency.valueHz)
    }

    @Test
    fun `update clamps frequency above supported range`() {
        ToneFrequency.update(MAX_TONE_FREQUENCY_HZ + TONE_FREQUENCY_STEP_HZ)

        assertEquals(MAX_TONE_FREQUENCY_HZ, ToneFrequency.valueHz)
    }
}
