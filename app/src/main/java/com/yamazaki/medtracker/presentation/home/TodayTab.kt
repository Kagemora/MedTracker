package com.yamazaki.medtracker.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodayTab(
    is24Hour: Boolean,
    state: HomeState.Content,
    onCommand: (HomeCommand) -> Unit
) {
    val allLogs = state.pendingLogs + state.takenLogs + state.skippedLogs

    if (allLogs.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Rounded.MedicalServices,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Нет лекарств на сегодня",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Добавьте лекарство во вкладке Поиск",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (state.pendingLogs.isNotEmpty()) {
            item {
                SectionHeader(title = "Ожидают приёма")
            }
            items(
                state.pendingLogs,
                key = { it.id }
            ) { log ->
                MedicineLogCard(
                    log = log,
                    is24Hour = is24Hour,
                    onTake = {
                        onCommand(HomeCommand.TakeMedicine(log.id))
                    },
                    onSkip = {
                        onCommand(HomeCommand.SkipMedicine(log.id))
                    }
                )
            }
        }
        if (state.takenLogs.isNotEmpty()) {
            item {
                SectionHeader(title = "Принято")
            }
            items(
                state.takenLogs,
                key = { it.id }
            ) { log ->
                MedicineLogCard(
                    log = log,
                    is24Hour = is24Hour,
                    onTake = {},
                    onSkip = {}
                )
            }
        }
        if (state.skippedLogs.isNotEmpty()) {
            item {
                SectionHeader(title = "Пропущено")
            }
            items(
                state.skippedLogs,
                key = { it.id }
            ) { log ->
                MedicineLogCard(
                    log = log,
                    is24Hour = is24Hour,
                    onTake = {},
                    onSkip = {}
                )
            }
        }
    }
}