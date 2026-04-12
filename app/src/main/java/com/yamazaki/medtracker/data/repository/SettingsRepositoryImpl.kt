package com.yamazaki.medtracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.yamazaki.medtracker.domain.model.AppSettings
import com.yamazaki.medtracker.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    private val IS_24_HOUR_FORMAT = booleanPreferencesKey("is_24_hour_format")

    override fun getSettings(): Flow<AppSettings> =
        context.dataStore.data.map { prefs ->
            AppSettings(
                isDarkTheme = prefs[IS_DARK_THEME] ?: false,
                is24HourFormat = prefs[IS_24_HOUR_FORMAT] ?: true
            )
        }

    override suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_DARK_THEME] = enabled
        }
    }

    override suspend fun set24HourFormat(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_24_HOUR_FORMAT] = enabled
        }
    }
}