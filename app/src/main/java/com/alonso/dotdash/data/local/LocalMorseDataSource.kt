package com.alonso.dotdash.data.local

import com.alonso.dotdash.domain.model.MorseAlphabet
import com.alonso.dotdash.domain.model.MorseSymbol
import com.alonso.dotdash.domain.model.SymbolCategory

object LocalMorseDataSource {
    val symbols = listOf(
        MorseSymbol(
            id = "ENG_A",
            symbol = "A",
            morseCode = ".-",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ),
        MorseSymbol(
            id = "ENG_B",
            symbol = "B",
            morseCode = "-...",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ),
        MorseSymbol(
            id = "RUS_A",
            symbol = "А",
            morseCode = ".-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ),
        MorseSymbol(
            id = "RUS_B",
            symbol = "Б",
            morseCode = "-...",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        )
    )
}
