package com.alonso.dotdash.domain.model

import java.util.Locale

data class LocalizedMeaning(
    val en: String,
    val ru: String
) {
    fun current(): String {
        return if (Locale.getDefault().language == "ru") ru else en
    }
}
