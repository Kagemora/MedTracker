package com.yamazaki.medtracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamazaki.medtracker.presentation.navigation.NavGraph
import com.yamazaki.medtracker.presentation.ui.theme.MedTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }
        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            MedTrackerTheme(isDarkTheme = settings.isDarkTheme) {
                NavGraph()
            }
        }
    }
}