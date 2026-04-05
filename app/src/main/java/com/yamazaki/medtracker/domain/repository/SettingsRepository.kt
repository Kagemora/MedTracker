package com.yamazaki.medtracker.domain.repository

import com.yamazaki.medtracker.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<AppSettings>
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun set24HourFormat(enabled: Boolean)
}