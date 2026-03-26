package com.alonso.dotdash.data.local

data class DictionaryItemUi(
    val id: String,
    val symbol: String,
    val morseCode: String
)

val mockDictionaryItems = listOf(
    DictionaryItemUi("A", "A", ".-"),
    DictionaryItemUi("B", "B", "-..."),
    DictionaryItemUi("C", "C", "-.-."),
    DictionaryItemUi("D", "D", "-.."),
    DictionaryItemUi("E", "E", "."),
    DictionaryItemUi("F", "F", "..-."),
    DictionaryItemUi("1", "1", ".----"),
    DictionaryItemUi("2", "2", "..---"),
    DictionaryItemUi("3", "3", "...--")
)
