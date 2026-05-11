package com.yamazaki.medtracker.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Настройки") })
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SettingsSectionHeader(title = "Внешний вид")
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                SettingsToggleItem(
                    icon = Icons.Rounded.DarkMode,
                    title = "Тёмная тема",
                    subtitle = "Переключить на тёмное оформление",
                    checked = settings.isDarkTheme,
                    onCheckedChange = viewModel::toggleDarkTheme
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSectionHeader(title = "Формат времени")
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                SettingsToggleItem(
                    icon = Icons.Rounded.AccessTime,
                    title = "24-часовой формат",
                    subtitle = if (settings.is24HourFormat) "Сейчас: 14:30" else "Сейчас: 2:30 PM",
                    checked = settings.is24HourFormat,
                    onCheckedChange = viewModel::toggle24HourFormat
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSectionHeader(title = "О приложении")
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                SettingsInfoItem(
                    icon = Icons.Rounded.Info,
                    title = "MedTracker",
                    subtitle = "Версия 1.0.0"
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsInfoItem(
                    icon = Icons.Rounded.Info,
                    title = "Данные о лекарствах",
                    subtitle = "Предоставлены OpenFDA"
                )
            }
        }
    }
}