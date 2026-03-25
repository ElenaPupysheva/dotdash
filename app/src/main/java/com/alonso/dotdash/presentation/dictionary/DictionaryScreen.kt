package com.alonso.dotdash.presentation.dictionary

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alonso.dotdash.R

@Composable
fun DictionaryScreen() {
    Text(
        text = stringResource(R.string.dictionary),
        style = MaterialTheme.typography.titleMedium
    )
}