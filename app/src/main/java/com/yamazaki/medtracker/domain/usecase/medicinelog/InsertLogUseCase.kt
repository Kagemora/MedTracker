package com.yamazaki.medtracker.domain.usecase.medicinelog

import com.yamazaki.medtracker.domain.model.MedicineLog
import com.yamazaki.medtracker.domain.repository.MedicineLogRepository
import javax.inject.Inject

class InsertLogUseCase @Inject constructor(
    private val medicineLogRepository: MedicineLogRepository
) {

    suspend operator fun invoke(log: MedicineLog) =
        medicineLogRepository.insertLog(log)
}