package com.alonso.dotdash.core.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.domain.model.MorseAlphabet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingTypeCard(
    title: String,
    alphabets: List<MorseAlphabet>,
    selectedAlphabet: MorseAlphabet,
    onAlphabetSelected: (MorseAlphabet) -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                alphabets.forEachIndexed { index, alphabet ->
                    SegmentedButton(
                        selected = alphabet == selectedAlphabet,
                        onClick = { onAlphabetSelected(alphabet) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = alphabets.size
                        )
                    ) {
                        Text(alphabet.title())
                    }
                }
            }

            Button(
                onClick = onPlayClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.play))
            }
        }
    }
}

private fun MorseAlphabet.title(): String = when (this) {
    MorseAlphabet.RUS -> "RU"
    MorseAlphabet.ENG -> "EN"
    MorseAlphabet.DIGITS -> "123"
}
