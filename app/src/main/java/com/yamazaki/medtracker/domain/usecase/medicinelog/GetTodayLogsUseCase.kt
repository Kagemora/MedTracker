package com.yamazaki.medtracker.domain.usecase.medicinelog

import com.yamazaki.medtracker.domain.repository.MedicineLogRepository
import javax.inject.Inject

class GetTodayLogsUseCase @Inject constructor(
    private val medicineLogRepository: MedicineLogRepository
) {

    operator fun invoke() =
        medicineLogRepository.getTodayLogs()
}