package com.yamazaki.medtracker.domain.usecase.settings

import com.yamazaki.medtracker.domain.repository.SettingsRepository
import javax.inject.Inject

class Set24HourFormatUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) =
        repository.set24HourFormat(enabled)
}