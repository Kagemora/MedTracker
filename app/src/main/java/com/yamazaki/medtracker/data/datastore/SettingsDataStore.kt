package com.yamazaki.medtracker.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.yamazaki.medtracker.domain.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    private val IS_24_HOUR_FORMAT = booleanPreferencesKey("is_24_hour_format")

    fun settings(): Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            isDarkTheme = prefs[IS_DARK_THEME] ?: false,
            is24HourFormat = prefs[IS_24_HOUR_FORMAT] ?: true
        )
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_DARK_THEME] = enabled
        }
    }

    suspend fun set24HourFormat(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_24_HOUR_FORMAT] = enabled
        }
    }
}