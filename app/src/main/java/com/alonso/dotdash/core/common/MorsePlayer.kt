package com.alonso.dotdash.core.common

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MorsePlayer(
    private val soundPlayer: SoundPlayer
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var playJob: Job? = null

    fun play(
        morseCode: String,
        onFinished: (() -> Unit)? = null
    ) {
        playJob?.cancel()

        playJob = scope.launch {
            try {
                morseCode.forEachIndexed { index, symbol ->
                    when (symbol) {
                        '.' -> {
                            soundPlayer.shortBeep()
                            delay(MorseTiming.DOT_DURATION_MS)
                        }

                        '-' -> {
                            soundPlayer.longBeep()
                            delay(MorseTiming.DASH_DURATION_MS)
                        }

                        ' ' -> {
                            delay(MorseTiming.LETTER_GAP_MS)
                        }
                    }

                    val isLast = index == morseCode.lastIndex
                    if (!isLast && symbol != ' ') {
                        delay(MorseTiming.SYMBOL_GAP_MS)
                    }
                }

                onFinished?.invoke()
            } catch (_: CancellationException) {
            }
        }
    }

    fun stop() {
        playJob?.cancel()
        playJob = null
    }

    fun release() {
        stop()
        scope.cancel()
        soundPlayer.release()
    }
}
