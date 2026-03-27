package com.alonso.dotdash.presentation.dictionary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.core.ui.DictionaryCard
import com.alonso.dotdash.data.local.LocalMorseDataSource
import com.alonso.dotdash.domain.model.MorseSymbol

const val COLUMNSIZE = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen() {
    val engItems = LocalMorseDataSource.englishSymbols
    val rusItems = LocalMorseDataSource.russianSymbols
    val digItems = LocalMorseDataSource.digitsSymbols
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = stringResource(R.string.dictionary),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        TabRowComponent(
            tabs = listOf("Eng", "Rus", "Dig"),
            contentScreens = listOf(
                { DictionaryGrid(engItems) },
                { DictionaryGrid(rusItems) },
                { DictionaryGrid(digItems) }
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            containerColor = Color.Gray,
            contentColor = Color.White,
            indicatorColor = Color.DarkGray
        )

    }
}

@Composable
fun DictionaryGrid(dictionaryItems: List<MorseSymbol>) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(16.dp),
        columns = GridCells.Fixed(COLUMNSIZE),
        contentPadding = PaddingValues(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dictionaryItems, key = { it.id }) { item ->
            DictionaryCard(
                symbol = item.symbol,
                morseCode = item.morseCode,
                onPlayClick = {}
            )
        }
    }
}

@Composable
fun TabRowComponent(
    tabs: List<String>,
    contentScreens: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Gray,
    contentColor: Color = Color.White,
    indicatorColor: Color = Color.DarkGray
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            contentColor = contentColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = indicatorColor
                )
            }
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    modifier = Modifier.padding(all = 16.dp),
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                ) {
                    Text(text = tabTitle)
                }
            }
        }
        contentScreens.getOrNull(selectedTabIndex)?.invoke()
    }
}
