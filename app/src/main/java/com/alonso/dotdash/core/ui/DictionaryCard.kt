package com.alonso.dotdash.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R

@Composable
fun DictionaryCard(
    symbol: String,
    morseCode: String,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isToggled by remember { mutableStateOf(false) }

    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = modifier
            .width(90.dp)
            .height(120.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = morseCode,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(4.dp))

            IconButton(
                onClick = {
                    isToggled = !isToggled
                    onPlayClick()
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (isToggled) R.drawable.pause
                        else R.drawable.play_arrow_filled
                    ),
                    modifier = Modifier.size(24.dp),
                    contentDescription = if (isToggled) {
                        "Pause morse sound"
                    } else {
                        "Play morse sound"
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DictionaryCardPreview() {
    DictionaryCard(
        symbol = "B",
        morseCode = "-...",
        onPlayClick = {}
    )
}
