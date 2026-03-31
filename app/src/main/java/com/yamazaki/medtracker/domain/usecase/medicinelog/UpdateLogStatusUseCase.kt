package com.yamazaki.medtracker.domain.usecase.medicinelog

import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.repository.MedicineLogRepository
import javax.inject.Inject

class UpdateLogStatusUseCase @Inject constructor(
    private val medicineLogRepository: MedicineLogRepository
) {

    suspend operator fun invoke(logId: Long, status: LogStatus, takenAt: Long?) =
        medicineLogRepository.updateLogStatus(logId, status, takenAt)
}