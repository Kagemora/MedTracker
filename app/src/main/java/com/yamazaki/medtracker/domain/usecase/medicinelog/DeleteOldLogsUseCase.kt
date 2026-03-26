package com.yamazaki.medtracker.domain.usecase.medicinelog

import com.yamazaki.medtracker.domain.repository.MedicineLogRepository
import javax.inject.Inject

class DeleteOldLogsUseCase @Inject constructor(
    private val medicineLogRepository: MedicineLogRepository
) {

    suspend operator fun invoke(olderThan: Long) =
        medicineLogRepository.deleteOldLogs(olderThan)
}