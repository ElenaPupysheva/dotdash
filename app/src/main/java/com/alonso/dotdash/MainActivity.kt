package com.alonso.dotdash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alonso.dotdash.core.navigation.Navigation
import com.alonso.dotdash.ui.theme.DotdashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotdashTheme {
                Navigation()
            }
        }
    }
}

