package com.alonso.dotdash.presentation.statistics

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alonso.dotdash.R

@Composable
fun StatisticScreen() {
    Text(
        text = stringResource(R.string.statistic),
        style = MaterialTheme.typography.titleMedium
    )
}
