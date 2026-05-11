package com.yamazaki.medtracker.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreenContent(
    is24Hour: Boolean,
    paddingValues: PaddingValues,
    state: HomeState.Content,
    onCommand: (HomeCommand) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        PrimaryTabRow(selectedTabIndex = state.selectedTab.ordinal) {
            Tab(
                selected = state.selectedTab == HomeViewModel.Tab.TODAY,
                onClick = {
                    onCommand(HomeCommand.SelectTab(HomeViewModel.Tab.TODAY))
                },
                text = {
                    Text("Сегодня")
                }
            )
            Tab(
                selected = state.selectedTab == HomeViewModel.Tab.SCHEDULES,
                onClick = {
                    onCommand(HomeCommand.SelectTab(HomeViewModel.Tab.SCHEDULES))
                },
                text = {
                    Text("Расписания")
                }
            )
        }

        when (state.selectedTab) {
            HomeViewModel.Tab.TODAY -> TodayTab(
                is24Hour = is24Hour,
                state = state,
                onCommand = onCommand
            )

            HomeViewModel.Tab.SCHEDULES -> SchedulesTab(
                is24Hour = is24Hour,
                state = state,
                onCommand = onCommand
            )
        }
    }
}
