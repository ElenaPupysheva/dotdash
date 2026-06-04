package com.alonso.dotdash.presentation.dictionary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.core.common.MorsePlayer
import com.alonso.dotdash.core.common.ToneBeepPlayer
import com.alonso.dotdash.core.ui.DictionaryCard
import com.alonso.dotdash.data.local.LocalMorseDataSource
import com.alonso.dotdash.domain.model.MorseSymbol
import com.alonso.dotdash.ui.theme.surfaceContainerLowLight

private const val DICTIONARY_COLUMN_SIZE = 2

private data class DictionaryTabUi(
    val title: String,
    val count: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    onBackClick: () -> Unit
) {
    val engItems = LocalMorseDataSource.englishSymbols
    val rusItems = LocalMorseDataSource.russianSymbols
    val digItems = LocalMorseDataSource.digitsSymbols

    val tabs = listOf(
        DictionaryTabUi(title = "Латиница", count = engItems.size),
        DictionaryTabUi(title = "Кириллица", count = rusItems.size),
        DictionaryTabUi(title = "Цифры", count = digItems.size)
    )

    val soundPlayer = remember { ToneBeepPlayer() }
    val morsePlayer = remember { MorsePlayer(soundPlayer) }
    var playingItemId by remember { mutableStateOf<String?>(null) }
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    fun stopPlayback() {
        morsePlayer.stop()
        playingItemId = null
    }

    fun handlePlayClick(item: MorseSymbol) {
        if (playingItemId == item.id) {
            stopPlayback()
        } else {
            morsePlayer.stop()
            playingItemId = item.id
            morsePlayer.play(item.morseCode) {
                if (playingItemId == item.id) {
                    playingItemId = null
                }
            }
        }
    }

    val sourceItems = when (selectedTabIndex) {
        0 -> engItems
        1 -> rusItems
        else -> digItems
    }

    val normalizedQuery = searchQuery.text.trim()
    val filteredItems = remember(sourceItems, normalizedQuery) {
        if (normalizedQuery.isBlank()) {
            sourceItems
        } else {
            sourceItems.filter { item ->
                item.symbol.contains(normalizedQuery, ignoreCase = true) ||
                        item.morseCode.contains(normalizedQuery)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            morsePlayer.stop()
            morsePlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = stringResource(R.string.dictionary),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            SpacerHeight(4.dp)

            DictionarySearchBar(
                value = searchQuery,
                onValueChange = {
                    stopPlayback()
                    searchQuery = it
                }
            )

            SpacerHeight(12.dp)

            DictionarySegmentedTabs(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index ->
                    selectedTabIndex = index
                    stopPlayback()
                }
            )

            SpacerHeight(16.dp)

            DictionaryGrid(
                dictionaryItems = filteredItems,
                playingItemId = playingItemId,
                onPlayClick = ::handlePlayClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun DictionarySearchBar(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = surfaceContainerLowLight,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.text.isEmpty()) {
                        Text(
                            text = "Найти букву или код",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
private fun DictionarySegmentedTabs(
    tabs: List<DictionaryTabUi>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                val selected = index == selectedTabIndex

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (selected) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                    shadowElevation = if (selected) 2.dp else 0.dp,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clickable { onTabSelected(index) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            maxLines = 1
                        )

                        Text(
                            text = tab.count.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DictionaryGrid(
    dictionaryItems: List<MorseSymbol>,
    playingItemId: String?,
    onPlayClick: (MorseSymbol) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(DICTIONARY_COLUMN_SIZE),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dictionaryItems, key = { it.id }) { item ->
            DictionaryCard(
                symbol = item.symbol,
                morseCode = item.morseCode,
                isPlaying = playingItemId == item.id,
                onPlayClick = { onPlayClick(item) }
            )
        }
    }
}

@Composable
private fun SpacerHeight(height: androidx.compose.ui.unit.Dp) {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(height))
}
