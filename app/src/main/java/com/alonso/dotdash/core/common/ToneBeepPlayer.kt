package com.alonso.dotdash.core.common

import android.media.AudioManager
import android.media.ToneGenerator

class ToneBeepPlayer : SoundPlayer {
    private val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    override fun shortBeep() {
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
    }

    override fun longBeep() {
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 400)
    }

    fun release() {
        toneGen.release()
    }
}