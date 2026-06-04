package com.alonso.dotdash.domain.model

data class MorseSymbol(
    val id: String,
    val symbol: String,
    val morseCode: String,
    val alphabet: MorseAlphabet,
    val category: SymbolCategory
)
