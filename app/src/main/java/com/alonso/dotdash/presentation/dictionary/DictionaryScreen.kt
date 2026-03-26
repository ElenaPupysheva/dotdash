package com.alonso.dotdash.presentation.dictionary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.core.ui.DictionaryCard
import com.alonso.dotdash.data.local.mockDictionaryItems

const val COLUMNSIZE = 3
@Composable
fun DictionaryScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.dictionary),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            columns = GridCells.Fixed(COLUMNSIZE),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mockDictionaryItems, key = { it.id }) { item ->
                DictionaryCard(
                    symbol = item.symbol,
                    morseCode = item.morseCode,
                    onPlayClick = {}
                )
            }
        }
    }
}
