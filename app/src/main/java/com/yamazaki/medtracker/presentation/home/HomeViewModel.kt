package com.yamazaki.medtracker.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.model.MedicineLog
import com.yamazaki.medtracker.domain.model.Schedule
import com.yamazaki.medtracker.domain.usecase.medicinelog.GetTodayLogsUseCase
import com.yamazaki.medtracker.domain.usecase.medicinelog.UpdateLogStatusUseCase
import com.yamazaki.medtracker.domain.usecase.notification.CancelNotificationUseCase
import com.yamazaki.medtracker.domain.usecase.schedule.DeleteScheduleUseCase
import com.yamazaki.medtracker.domain.usecase.schedule.GetAllActiveSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayLogsUseCase: GetTodayLogsUseCase,
    private val updateLogStatusUseCase: UpdateLogStatusUseCase,
    private val getAllActiveSchedulesUseCase: GetAllActiveSchedulesUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
    private val cancelNotificationUseCase: CancelNotificationUseCase
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(Tab.TODAY)

    val state: StateFlow<HomeState> = combine(
        getTodayLogsUseCase(),
        getAllActiveSchedulesUseCase(),
        _selectedTab
    ) { logs, schedules, tab ->
        HomeState.Content(
            pendingLogs = logs.filter { it.status == LogStatus.PENDING },
            takenLogs = logs.filter { it.status == LogStatus.TAKEN },
            skippedLogs = logs.filter { it.status == LogStatus.SKIPPED },
            schedules = schedules,
            selectedTab = tab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeState.Loading
    )

    fun processCommand(command: HomeCommand) {
        when (command) {
            is HomeCommand.TakeMedicine -> takeMedicine(command.logId)
            is HomeCommand.SkipMedicine -> skipMedicine(command.logId)
            is HomeCommand.DeleteSchedule -> deleteSchedule(command.scheduleId)
            is HomeCommand.SelectTab -> selectTab(command.tab)
        }
    }

    private fun takeMedicine(logId: Long) {
        viewModelScope.launch {
            updateLogStatusUseCase(logId, LogStatus.TAKEN, System.currentTimeMillis())
        }
    }

    private fun skipMedicine(logId: Long) {
        viewModelScope.launch {
            updateLogStatusUseCase(logId, LogStatus.SKIPPED, null)
        }
    }

    private fun deleteSchedule(scheduleId: Long) {
        viewModelScope.launch {
            cancelNotificationUseCase(scheduleId)
            deleteScheduleUseCase(scheduleId)
        }
    }

    private fun selectTab(tab: Tab) {
        _selectedTab.update {
            tab
        }
    }

    enum class Tab { TODAY, SCHEDULES }
}

sealed interface HomeState {
    data object Loading : HomeState
    data class Content(
        val pendingLogs: List<MedicineLog> = emptyList(),
        val takenLogs: List<MedicineLog> = emptyList(),
        val skippedLogs: List<MedicineLog> = emptyList(),
        val schedules: List<Schedule> = emptyList(),
        val selectedTab: HomeViewModel.Tab = HomeViewModel.Tab.TODAY
    ) : HomeState

    data class Error(val message: String) : HomeState
}

sealed interface HomeCommand {
    data class TakeMedicine(val logId: Long) : HomeCommand
    data class SkipMedicine(val logId: Long) : HomeCommand
    data class SelectTab(val tab: HomeViewModel.Tab) : HomeCommand
    data class DeleteSchedule(val scheduleId: Long) : HomeCommand
}