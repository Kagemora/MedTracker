package com.yamazaki.medtracker.domain.usecase.settings

import com.yamazaki.medtracker.domain.model.AppSettings
import com.yamazaki.medtracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<AppSettings> =
        repository.getSettings()
}