package com.alonso.dotdash.presentation.training

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.core.common.ToneBeepPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val DASH_THRESHOLD_MS = 260L
private const val LETTER_GAP_MS = 700L
private const val WORD_GAP_MS = 1_500L

@Composable
fun MorseInputPad(
    value: String,
    onAppend: (String) -> Unit,
    onDelete: () -> Unit,
    autoSpaces: Boolean = false
) {
    val scope = rememberCoroutineScope()
    var gapJob by remember { mutableStateOf<Job?>(null) }
    var isPressed by remember { mutableStateOf(false) }
    var soundEnabled by rememberSaveable { mutableStateOf(true) }
    val tonePlayer = remember { ToneBeepPlayer() }

    DisposableEffect(tonePlayer) {
        onDispose {
            tonePlayer.stopTone()
            tonePlayer.release()
        }
    }

    fun scheduleGap() {
        if (!autoSpaces) return
        gapJob?.cancel()
        gapJob = scope.launch {
            delay(LETTER_GAP_MS.milliseconds)
            onAppend(" ")
            delay((WORD_GAP_MS - LETTER_GAP_MS).milliseconds)
            onAppend("  ")
        }
    }

    fun appendLetterSpace() {
        gapJob?.cancel()
        if (value.isBlank()) return

        val trailingSpaces = value.length - value.trimEnd().length
        if (trailingSpaces == 0) {
            onAppend(" ")
        }
    }

    fun appendWordSpace() {
        gapJob?.cancel()
        if (value.isBlank()) return

        val trailingSpaces = value.length - value.trimEnd().length
        if (trailingSpaces < 3) {
            onAppend(" ".repeat(3 - trailingSpaces))
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (isPressed) {
                "Удерживайте для тире"
            } else {
                "Коротко — точка · Долго — тире —"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .size(132.dp)
                .clip(CircleShape)
                .background(
                    if (isPressed) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                )
                .pointerInput(onAppend, autoSpaces, soundEnabled) {
                    detectTapGestures(
                        onPress = {
                            gapJob?.cancel()
                            isPressed = true
                            if (soundEnabled) {
                                tonePlayer.startContinuousTone()
                            }

                            val startedAt = SystemClock.elapsedRealtime()
                            val released = tryAwaitRelease()

                            tonePlayer.stopTone()
                            val duration = SystemClock.elapsedRealtime() - startedAt
                            isPressed = false

                            if (released) {
                                onAppend(if (duration >= DASH_THRESHOLD_MS) "-" else ".")
                                scheduleGap()
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isPressed) "—" else "Нажать",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isPressed) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }
            )
        }
        Text(
            text = "Добавьте пробел: буква или слово",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = ::appendLetterSpace,
                modifier = Modifier.height(40.dp),
                contentPadding = ButtonDefaults.ContentPadding
            ) {
                Text(
                    text = stringResource(R.string.letter_space),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            OutlinedButton(
                onClick = ::appendWordSpace,
                modifier = Modifier.height(40.dp),
                contentPadding = ButtonDefaults.ContentPadding
            ) {
                Text(
                    text = stringResource(R.string.word_space),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    gapJob?.cancel()
                    onDelete()
                },
                modifier = Modifier.height(40.dp),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Удалить",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                IconButton(
                    onClick = {
                        soundEnabled = !soundEnabled
                        if (!soundEnabled) {
                            tonePlayer.stopTone()
                        }
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (soundEnabled) {
                            Icons.AutoMirrored.Filled.VolumeUp
                        } else {
                            Icons.AutoMirrored.Filled.VolumeOff
                        },
                        contentDescription = if (soundEnabled) {
                            "Звук включён"
                        } else {
                            "Звук выключен"
                        },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
