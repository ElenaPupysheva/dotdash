package com.alonso.dotdash.presentation.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.core.ui.DifficultySelector
import com.alonso.dotdash.core.ui.TrainingTypeCard
import com.alonso.dotdash.domain.model.MorseAlphabet
import com.alonso.dotdash.domain.model.TrainingDifficulty
import com.alonso.dotdash.domain.model.TrainingGameType
import com.alonso.dotdash.domain.model.TrainingTypes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BufferScreen(
    onBackClick: () -> Unit,
    onPlayClick: (TrainingGameType, MorseAlphabet?, TrainingDifficulty) -> Unit
) {
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
                        text = stringResource(R.string.training),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            BufferGrid(
                games = TrainingTypes.games,
                onPlayClick = onPlayClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private val TRAINING_CARD_MIN_WIDTH = 320.dp
private val TRAINING_CARD_MIN_HEIGHT = 180.dp

@Composable
fun BufferGrid(
    games: List<TrainingTypes>,
    onPlayClick: (TrainingGameType, MorseAlphabet?, TrainingDifficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = TRAINING_CARD_MIN_WIDTH),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = games,
            key = { it.type }
        ) { game ->
            when (game.type) {
                TrainingGameType.CLASSIC -> {
                    var selectedAlphabet by rememberSaveable(game.type.name) {
                        mutableStateOf(game.alphabets.first())
                    }
                    var difficulty by rememberSaveable("${game.type.name}_difficulty") {
                        mutableStateOf(TrainingDifficulty.NORMAL)
                    }

                    TrainingTypeCard(
                        title = stringResource(R.string.classic_training),
                        alphabets = game.alphabets,
                        selectedAlphabet = selectedAlphabet,
                        onAlphabetSelected = { selectedAlphabet = it },
                        selectedDifficulty = difficulty,
                        onDifficultySelected = { difficulty = it },
                        onPlayClick = {
                            onPlayClick(game.type, selectedAlphabet, difficulty)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = TRAINING_CARD_MIN_HEIGHT)
                    )
                }

                TrainingGameType.QCODE -> {
                    ModeTrainingCard(stringResource(R.string.q_code_training)) { difficulty ->
                        onPlayClick(game.type, null, difficulty)
                    }
                }

                TrainingGameType.GREETINGS -> {
                    ModeTrainingCard(stringResource(R.string.greetings_training)) { difficulty ->
                        onPlayClick(game.type, null, difficulty)
                    }
                }

                TrainingGameType.FREE_WRITING -> {
                    SimpleTrainingCard(stringResource(R.string.free_writing)) {
                        onPlayClick(game.type, null, TrainingDifficulty.HARD)
                    }
                }
            }
        }
    }
}

@Composable
private fun ModeTrainingCard(title: String, onPlayClick: (TrainingDifficulty) -> Unit) {
    var difficulty by rememberSaveable(title) { mutableStateOf(TrainingDifficulty.NORMAL) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = TRAINING_CARD_MIN_HEIGHT)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )

            DifficultySelector(difficulty) { difficulty = it }

            Button(
                onClick = { onPlayClick(difficulty) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.play))
            }
        }
    }
}


@Composable
private fun SimpleTrainingCard(title: String, onPlayClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 132.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            Button(onClick = onPlayClick, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.play))
            }
        }
    }
}

