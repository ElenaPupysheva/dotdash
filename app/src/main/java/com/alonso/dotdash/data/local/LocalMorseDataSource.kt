package com.alonso.dotdash.data.local

import com.alonso.dotdash.domain.model.LocalizedMeaning
import com.alonso.dotdash.domain.model.MorseAlphabet
import com.alonso.dotdash.domain.model.MorseSymbol
import com.alonso.dotdash.domain.model.SymbolCategory

object LocalMorseDataSource {
    val englishSymbols = listOf(
        MorseSymbol(
            id = "ENG_A",
            symbol = "A",
            morseCode = ".-",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_B",
            symbol = "B",
            morseCode = "-...",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_C",
            symbol = "C",
            morseCode = "-.-.",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_D",
            symbol = "D",
            morseCode = "-..",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_E",
            symbol = "E",
            morseCode = ".",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_F",
            symbol = "F",
            morseCode = "..-.",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_G",
            symbol = "G",
            morseCode = "--.",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_H",
            symbol = "H",
            morseCode = "....",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_I",
            symbol = "I",
            morseCode = "..",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_J",
            symbol = "J",
            morseCode = ".---",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_K",
            symbol = "K",
            morseCode = "-.-",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_L",
            symbol = "L",
            morseCode = ".-..",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_M",
            symbol = "M",
            morseCode = "--",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_N",
            symbol = "N",
            morseCode = "-.",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_O",
            symbol = "O",
            morseCode = "---",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_P",
            symbol = "P",
            morseCode = ".--.",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_Q",
            symbol = "Q",
            morseCode = "--.-",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_R",
            symbol = "R",
            morseCode = ".-.",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_S",
            symbol = "S",
            morseCode = "...",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_T",
            symbol = "T",
            morseCode = "-",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_U",
            symbol = "U",
            morseCode = "..-",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_V",
            symbol = "V",
            morseCode = "...-",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_W",
            symbol = "W",
            morseCode = ".--",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_X",
            symbol = "X",
            morseCode = "-..-",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_Y",
            symbol = "Y",
            morseCode = "-.--",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "ENG_Z",
            symbol = "Z",
            morseCode = "--..",
            alphabet = MorseAlphabet.ENG,
            category = SymbolCategory.LETTER
        )
    )
    val russianSymbols = listOf(
        MorseSymbol(
            id = "RUS_A",
            symbol = "А",
            morseCode = ".-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Б",
            symbol = "Б",
            morseCode = "-...",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_В",
            symbol = "В",
            morseCode = ".--",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Г",
            symbol = "Г",
            morseCode = "--.",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Д",
            symbol = "Д",
            morseCode = "-..",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Е",
            symbol = "Е",
            morseCode = ".",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ж",
            symbol = "Ж",
            morseCode = "...-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_З",
            symbol = "З",
            morseCode = "--..",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_И",
            symbol = "И",
            morseCode = "..",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Й",
            symbol = "Й",
            morseCode = ".---",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_К",
            symbol = "К",
            morseCode = "-.-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Л",
            symbol = "Л",
            morseCode = ".-..",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_М",
            symbol = "М",
            morseCode = "--",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Н",
            symbol = "Н",
            morseCode = "-.",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_О",
            symbol = "О",
            morseCode = "---",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_П",
            symbol = "П",
            morseCode = ".--.",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Р",
            symbol = "Р",
            morseCode = ".-.",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_С",
            symbol = "С",
            morseCode = "...",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Т",
            symbol = "Т",
            morseCode = "-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_У",
            symbol = "У",
            morseCode = "..-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ф",
            symbol = "Ф",
            morseCode = "..-.",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Х",
            symbol = "Х",
            morseCode = "....",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ц",
            symbol = "Ц",
            morseCode = "-.-.",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ч",
            symbol = "Ч",
            morseCode = "---.",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ш",
            symbol = "Ш",
            morseCode = "----",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Щ",
            symbol = "Щ",
            morseCode = "--.-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ъ",
            symbol = "Ъ",
            morseCode = ".--.-.",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ы",
            symbol = "Ы",
            morseCode = "-.--",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ь",
            symbol = "Ь",
            morseCode = "-..-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Э",
            symbol = "Э",
            morseCode = "..-..",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Ю",
            symbol = "Ю",
            morseCode = "..--",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        ), MorseSymbol(
            id = "RUS_Я",
            symbol = "Я",
            morseCode = ".-.-",
            alphabet = MorseAlphabet.RUS,
            category = SymbolCategory.LETTER
        )
    )
    val digitsSymbols = listOf(
        MorseSymbol(
            id = "DIG_0",
            symbol = "0",
            morseCode = "-----",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_1",
            symbol = "1",
            morseCode = ".----",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_2",
            symbol = "2",
            morseCode = "..---",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_3",
            symbol = "3",
            morseCode = "...--",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_4",
            symbol = "4",
            morseCode = "....-",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_5",
            symbol = "5",
            morseCode = ".....",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_6",
            symbol = "6",
            morseCode = "-....",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_7",
            symbol = "7",
            morseCode = "--...",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_8",
            symbol = "8",
            morseCode = "---..",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        ), MorseSymbol(
            id = "DIG_9",
            symbol = "9",
            morseCode = "----.",
            alphabet = MorseAlphabet.DIGITS,
            category = SymbolCategory.DIGIT
        )
    )

    val qcodeSymbols = listOf(
        MorseSymbol(
            "CODE_QTH",
            "QTH",
            "--.- - ....",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("What is your location?", "Каково ваше местоположение?")
        ), MorseSymbol(
            "CODE_QSL",
            "QSL",
            "--.- ... .-..",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("I acknowledge receipt", "Подтверждаю приём")
        ), MorseSymbol(
            "CODE_QRZ",
            "QRZ",
            "--.- .-. --..",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("Who is calling me?", "Кто меня вызывает?")
        ), MorseSymbol(
            "CODE_QRT",
            "QRT",
            "--.- .-. -",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("Stop sending", "Прекратите передачу")
        ), MorseSymbol(
            "CODE_QRX",
            "QRX",
            "--.- .-. -..-",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("Please wait", "Пожалуйста, подождите")
        ), MorseSymbol(
            "CODE_QSY",
            "QSY",
            "--.- ... -.--",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("Change frequency", "Перейдите на другую частоту")
        ), MorseSymbol(
            "CODE_QRM",
            "QRM",
            "--.- .-. --",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("Interference", "Помехи от других станций")
        ), MorseSymbol(
            "CODE_QRN",
            "QRN",
            "--.- .-. -.",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("Static interference", "Атмосферные помехи")
        ), MorseSymbol(
            "CODE_QSB",
            "QSB",
            "--.- ... -...",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("Signal strength varies", "Уровень сигнала меняется")
        ), MorseSymbol(
            "CODE_QTC",
            "QTC",
            "--.- - -.-.",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("I have a message to send", "У меня есть сообщение для передачи")
        ), MorseSymbol(
            "CODE_QTR",
            "QTR",
            "--.- - .-.",
            MorseAlphabet.ENG,
            SymbolCategory.QCODE,
            LocalizedMeaning("What time is it?", "Который час?")
        )
    )

    val greetingsSymbols = listOf(
        MorseSymbol(
            "CODE_73",
            "73",
            "--... ...--",
            MorseAlphabet.DIGITS,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Best wishes", "Наилучшие пожелания")
        ), MorseSymbol(
            "CODE_88",
            "88",
            "---.. ---..",
            MorseAlphabet.DIGITS,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Love and kisses", "С любовью и поцелуями")
        ), MorseSymbol(
            "CODE_TU",
            "TU",
            "- ..-",
            MorseAlphabet.ENG,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Thank you", "Спасибо")
        ), MorseSymbol(
            "CODE_GA",
            "GA",
            "--. .-",
            MorseAlphabet.ENG,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Good afternoon / Go ahead", "Добрый день / Продолжайте передачу")
        ), MorseSymbol(
            "CODE_GM",
            "GM",
            "--. --",
            MorseAlphabet.ENG,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Good morning", "Доброе утро")
        ), MorseSymbol(
            "CODE_GE",
            "GE",
            "--. .",
            MorseAlphabet.ENG,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Good evening", "Добрый вечер")
        ), MorseSymbol(
            "CODE_GN",
            "GN",
            "--. -.",
            MorseAlphabet.ENG,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Good night", "Спокойной ночи")
        ), MorseSymbol(
            "CODE_HI",
            "HI",
            ".... ..",
            MorseAlphabet.ENG,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Laughter", "Смех")
        ), MorseSymbol(
            "CODE_BCNU",
            "BCNU",
            "-... -.-. -. ..-",
            MorseAlphabet.ENG,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("Be seeing you", "Увидимся")
        ), MorseSymbol(
            "CODE_CUAGN",
            "CUAGN",
            "-.-. ..- .- --. -.",
            MorseAlphabet.ENG,
            SymbolCategory.GREETINGS,
            LocalizedMeaning("See you again", "До новой встречи")
        )
    )
    val symbols = englishSymbols + russianSymbols + digitsSymbols
}

