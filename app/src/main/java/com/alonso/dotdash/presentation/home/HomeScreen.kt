package com.alonso.dotdash.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alonso.dotdash.R
import com.alonso.dotdash.core.navigation.Screen
import com.alonso.dotdash.core.ui.HomeFab
import com.alonso.dotdash.ui.theme.PinPadBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PinPadBackgroundColor,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = stringResource(R.string.header),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)

        ) {
            HomeRow(
                buttonText = stringResource(R.string.training),
                descriptionText = stringResource(R.string.txt_training),
                icon = Icons.Filled.PlayArrow,
                onClick = { navController.navigate(Screen.TrainingScreen.route) },
                buttonFirst = true
            )
            HomeRow(
                buttonText = stringResource(R.string.dictionary),
                descriptionText = stringResource(R.string.txt_dictionary),
                icon = Icons.AutoMirrored.Filled.MenuBook,
                onClick = { navController.navigate(Screen.DictionaryScreen.route) },
                buttonFirst = false
            )
            HomeRow(
                buttonText = stringResource(R.string.settings),
                descriptionText = stringResource(R.string.txt_settings),
                icon = Icons.Filled.Settings,
                onClick = { navController.navigate(Screen.SettingsScreen.route) },
                buttonFirst = true
            )
            HomeRow(
                buttonText = stringResource(R.string.statistic),
                descriptionText = stringResource(R.string.txt_statistic),
                icon = Icons.Filled.BarChart,
                onClick = { navController.navigate(Screen.StatisticScreen.route) },
                buttonFirst = false
            )
        }
    }
}

@Composable
fun HomeRow(
    buttonText: String,
    descriptionText: String,
    icon: ImageVector,
    onClick: () -> Unit,
    buttonFirst: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (buttonFirst) {
            HomeFab(
                text = buttonText,
                icon = icon,
                onClick = onClick,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = descriptionText,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        } else {
            Text(
                text = descriptionText,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            HomeFab(
                text = buttonText,
                icon = icon,
                onClick = onClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
