package com.yamazaki.medtracker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamazaki.medtracker.domain.model.AppSettings
import com.yamazaki.medtracker.domain.usecase.settings.GetSettingsUseCase
import com.yamazaki.medtracker.domain.usecase.settings.Set24HourFormatUseCase
import com.yamazaki.medtracker.domain.usecase.settings.SetDarkThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val set24HourFormatUseCase: Set24HourFormatUseCase
) : ViewModel() {

    val settings: StateFlow<AppSettings> = getSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppSettings()
        )

    fun toggleDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            setDarkThemeUseCase(enabled)
        }
    }

    fun toggle24HourFormat(enabled: Boolean) {
        viewModelScope.launch {
            set24HourFormatUseCase(enabled)
        }
    }
}