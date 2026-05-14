package com.alonso.dotdash.core.common

private const val DASH_MULTIPLIER = 3L
private const val LETTER_GAP_MULTIPLIER = 3L

object MorseTiming {
    const val TIME_UNIT_MS = 150L
    const val DOT_DURATION_MS = TIME_UNIT_MS
    const val DASH_DURATION_MS = TIME_UNIT_MS * DASH_MULTIPLIER
    const val SYMBOL_GAP_MS = TIME_UNIT_MS
    const val LETTER_GAP_MS = TIME_UNIT_MS * LETTER_GAP_MULTIPLIER
}
