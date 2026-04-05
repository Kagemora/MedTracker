package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.data.datastore.SettingsDataStore
import com.yamazaki.medtracker.domain.model.AppSettings
import com.yamazaki.medtracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {
    override fun getSettings(): Flow<AppSettings> {
        return settingsDataStore.settings()
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        settingsDataStore.setDarkTheme(enabled)
    }

    override suspend fun set24HourFormat(enabled: Boolean) {
        settingsDataStore.set24HourFormat(enabled)
    }
}