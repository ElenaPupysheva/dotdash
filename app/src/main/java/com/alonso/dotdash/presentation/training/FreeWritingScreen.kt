package com.alonso.dotdash.presentation.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.data.local.LocalMorseDataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeWritingScreen(onBackClick: () -> Unit) {
    var input by rememberSaveable { mutableStateOf("") }
    val dictionary = remember {
        (LocalMorseDataSource.englishSymbols + LocalMorseDataSource.russianSymbols + LocalMorseDataSource.digitsSymbols)
            .associateBy({ it.morseCode }, { it.symbol })
    }
    val decoded = input.split("   ").joinToString(" ") { word ->
        word.trim().split(Regex(" +")).filter(String::isNotBlank)
            .joinToString("") { dictionary[it] ?: if (it.isBlank()) "" else "?" }
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground
            ),
            title = { Text(stringResource(R.string.free_writing)) },
            navigationIcon = {
                IconButton(onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        stringResource(R.string.back)
                    )
                }
            }
        )
    }) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(stringResource(R.string.your_text), style = MaterialTheme.typography.labelLarge)
            Surface(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 110.dp),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    decoded.ifEmpty { "…" },
                    Modifier.padding(20.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Surface(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    input.ifEmpty { "· · ·" },
                    Modifier.padding(20.dp),
                    fontFamily = FontFamily.Monospace
                )
            }
            MorseInputPad(
                onAppend = { input += it },
                onDelete = { if (input.isNotEmpty()) input = input.dropLast(1) })
            OutlinedButton(
                { input = "" },
                Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.clear)) }
        }
    }
}
