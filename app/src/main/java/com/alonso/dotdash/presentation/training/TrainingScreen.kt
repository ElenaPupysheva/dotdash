package com.alonso.dotdash.presentation.training

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alonso.dotdash.R
@Composable
fun TrainingScreen() {
    Text(
        text = stringResource(R.string.training),
        style = MaterialTheme.typography.titleMedium
    )
}
