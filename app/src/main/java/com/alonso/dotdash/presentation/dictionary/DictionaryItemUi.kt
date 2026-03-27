package com.alonso.dotdash.presentation.dictionary

import com.alonso.dotdash.domain.model.MorseAlphabet

data class DictionaryItemUi(
    val id: String,
    val symbol: String,
    val morseCode: String,
    val alphabet: MorseAlphabet
)

val mockDictionaryItems = listOf(
    DictionaryItemUi(
        id = "ENG_A",
        symbol = "A",
        morseCode = ".-",
        alphabet = MorseAlphabet.ENG
    ),
    DictionaryItemUi(
        id = "ENG_B",
        symbol = "B",
        morseCode = "-...",
        alphabet = MorseAlphabet.ENG
    ),
    DictionaryItemUi(
        id = "ENG_C",
        symbol = "C",
        morseCode = "-.-.",
        alphabet = MorseAlphabet.ENG
    ),
    DictionaryItemUi(
        id = "RUS_A",
        symbol = "А",
        morseCode = ".-",
        alphabet = MorseAlphabet.RUS
    ),
    DictionaryItemUi(
        id = "RUS_B",
        symbol = "Б",
        morseCode = "-...",
        alphabet = MorseAlphabet.RUS
    ),
    DictionaryItemUi(
        id = "RUS_V",
        symbol = "В",
        morseCode = ".--",
        alphabet = MorseAlphabet.RUS
    )
)
