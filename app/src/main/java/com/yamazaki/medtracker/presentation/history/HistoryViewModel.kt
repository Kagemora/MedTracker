package com.yamazaki.medtracker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamazaki.medtracker.domain.model.MedicineLog
import com.yamazaki.medtracker.domain.usecase.medicinelog.GetLogsByDateRangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getLogsByDateRangeUseCase: GetLogsByDateRangeUseCase
) : ViewModel() {

    data class HistoryUiState(
        val groupedLogs: Map<LocalDate, List<MedicineLog>> = emptyMap(),
        val isLoading: Boolean = true,
        val selectedPeriod: Period = Period.WEEK
    )

    enum class Period(val label: String, val days: Long) {
        WEEK("7 дней", 7),
        MONTH("30 дней", 30),
        THREE_MONTHS("3 месяца", 90)
    }

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadLogs(Period.WEEK)
    }

    fun selectPeriod(period: Period) {
        _uiState.update { it.copy(selectedPeriod = period, isLoading = true) }
        loadLogs(period)
    }

    private fun loadLogs(period: Period) {
        val now = System.currentTimeMillis()
        val from = now - period.days * 24 * 60 * 60 * 1000L

        getLogsByDateRangeUseCase(from, now)
            .onEach { logs ->
                val grouped = logs
                    .sortedByDescending { it.scheduledAt }
                    .groupBy { log ->
                        log.scheduledAt
                            .let { java.util.Date(it) }
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                _uiState.update {
                    it.copy(groupedLogs = grouped, isLoading = false)
                }
            }
            .launchIn(viewModelScope)
    }
}