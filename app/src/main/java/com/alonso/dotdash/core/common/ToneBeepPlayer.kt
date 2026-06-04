package com.alonso.dotdash.core.common

import android.media.AudioManager
import android.media.ToneGenerator

private const val TONE_VOLUME = 100

class ToneBeepPlayer : SoundPlayer {
    private val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, TONE_VOLUME)

    override fun shortBeep() {
        toneGen.startTone(
            ToneGenerator.TONE_PROP_BEEP,
            MorseTiming.DOT_DURATION_MS.toInt()
        )
    }

    override fun longBeep() {
        toneGen.startTone(
            ToneGenerator.TONE_PROP_BEEP,
            MorseTiming.DASH_DURATION_MS.toInt()
        )
    }

    override fun release() {
        toneGen.release()
    }
}
