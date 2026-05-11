package com.yamazaki.medtracker.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamazaki.medtracker.presentation.navigation.NavGraph
import com.yamazaki.medtracker.presentation.ui.theme.MedTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        requestBatteryOptimization()
        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }
        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()

            val isDark = settings.isDarkTheme

            SideEffect {
                val controller = WindowInsetsControllerCompat(window, window.decorView)
                window.navigationBarColor = if (isDark) {
                    android.graphics.Color.BLACK
                } else {
                    android.graphics.Color.WHITE
                }
                controller.isAppearanceLightNavigationBars = !isDark
            }
            MedTrackerTheme(isDarkTheme = settings.isDarkTheme) {
                NavGraph()
            }
        }
    }

    @SuppressLint("BatteryLife", "UseKtx")
    private fun requestBatteryOptimization() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }
}