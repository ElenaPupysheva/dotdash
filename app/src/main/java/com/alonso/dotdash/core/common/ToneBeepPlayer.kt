package com.alonso.dotdash.core.common

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

private const val SAMPLE_RATE = 44_100
private const val TONE_FREQUENCY_HZ = 650.0
private const val TONE_VOLUME = 0.55
private const val FADE_MS = 8L

class ToneBeepPlayer : SoundPlayer {
    private var activeTrack: AudioTrack? = null

    @Volatile
    private var continuousToneEnabled = false
    private var continuousToneThread: Thread? = null

    override fun shortBeep() {
        playTone(MorseTiming.DOT_DURATION_MS)
    }

    override fun longBeep() {
        playTone(MorseTiming.DASH_DURATION_MS)
    }

    fun startContinuousTone() {
        stopTone()
        continuousToneEnabled = true

        continuousToneThread = Thread {
            val bufferSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(SAMPLE_RATE / 10)

            val track = createAudioTrack(bufferSize, AudioTrack.MODE_STREAM)
            activeTrack = track
            track.play()

            val buffer = ShortArray(bufferSize / 2)
            var sampleIndex = 0

            while (continuousToneEnabled) {
                for (index in buffer.indices) {
                    buffer[index] = sineSample(sampleIndex++)
                }
                track.write(buffer, 0, buffer.size)
            }

            track.pause()
            track.flush()
            track.release()
            if (activeTrack == track) {
                activeTrack = null
            }
        }.apply {
            isDaemon = true
            start()
        }
    }

    fun stopTone() {
        continuousToneEnabled = false

        if (continuousToneThread != null) {
            continuousToneThread = null
            return
        }

        activeTrack?.runCatching {
            stop()
            flush()
            release()
        }
        activeTrack = null
    }

    override fun release() {
        stopTone()
    }

    private fun playTone(durationMs: Long) {
        stopTone()

        val samples = createToneSamples(durationMs)
        val track = createAudioTrack(samples.size * Short.SIZE_BYTES, AudioTrack.MODE_STATIC)
        activeTrack = track
        track.write(samples, 0, samples.size)
        track.play()
    }

    private fun createAudioTrack(
        bufferSizeInBytes: Int,
        mode: Int
    ): AudioTrack {
        return AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build()
            )
            .setBufferSizeInBytes(bufferSizeInBytes)
            .setTransferMode(mode)
            .build()
    }

    private fun createToneSamples(durationMs: Long): ShortArray {
        val sampleCount = (SAMPLE_RATE * durationMs / 1_000).toInt()
        val fadeSamples = (SAMPLE_RATE * FADE_MS / 1_000).toInt().coerceAtLeast(1)

        return ShortArray(sampleCount) { index ->
            val fadeIn = (index.toDouble() / fadeSamples).coerceIn(0.0, 1.0)
            val fadeOut = ((sampleCount - index).toDouble() / fadeSamples).coerceIn(0.0, 1.0)
            val envelope = minOf(fadeIn, fadeOut)
            sineSample(index, envelope)
        }
    }

    private fun sineSample(
        sampleIndex: Int,
        envelope: Double = 1.0
    ): Short {
        val angle = 2.0 * PI * TONE_FREQUENCY_HZ * sampleIndex / SAMPLE_RATE
        val value = sin(angle) * Short.MAX_VALUE * TONE_VOLUME * envelope
        return value.toInt().toShort()
    }
}

