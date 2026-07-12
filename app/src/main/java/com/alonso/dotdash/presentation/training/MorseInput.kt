package com.alonso.dotdash.presentation.training

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DASH_THRESHOLD_MS = 260L
private const val LETTER_GAP_MS = 700L
private const val WORD_GAP_MS = 1_500L

@Composable
fun MorseInputPad(
    onAppend: (String) -> Unit,
    onDelete: () -> Unit,
    showSpaces: Boolean = true
) {
    val scope = rememberCoroutineScope()
    var gapJob by remember { mutableStateOf<Job?>(null) }
    var isPressed by remember { mutableStateOf(false) }

    fun scheduleGap() {
        if (!showSpaces) return
        gapJob?.cancel()
        gapJob = scope.launch {
            delay(LETTER_GAP_MS)
            onAppend(" ")
            delay(WORD_GAP_MS - LETTER_GAP_MS)
            onAppend("  ")
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (isPressed) "Удерживайте для тире" else "Коротко — точка ·  Долго — тире —",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
            modifier = Modifier
                .size(148.dp)
                .clip(CircleShape)
                .background(
                    if (isPressed) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.primary
                )
                .pointerInput(onAppend, showSpaces) {
                    detectTapGestures(
                        onPress = {
                            gapJob?.cancel()
                            isPressed = true
                            val startedAt = SystemClock.elapsedRealtime()
                            val released = tryAwaitRelease()
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
                color = if (isPressed) MaterialTheme.colorScheme.onPrimaryContainer else Color.White
            )
        }
        OutlinedButton(onClick = {
            gapJob?.cancel()
            onDelete()
        }) {
            Text("⌫  Удалить")
        }
    }
}
