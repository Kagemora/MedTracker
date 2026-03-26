package com.yamazaki.medtracker.domain.usecase.medicinelog

import com.yamazaki.medtracker.domain.repository.MedicineLogRepository
import javax.inject.Inject

class GetLogsByDateRangeUseCase @Inject constructor(
    private val medicineLogRepository: MedicineLogRepository
) {

    operator fun invoke(from: Long, to: Long) =
        medicineLogRepository.getLogsByDateRange(from, to)
}