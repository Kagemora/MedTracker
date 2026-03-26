package com.yamazaki.medtracker.domain.usecase.medicine

import com.yamazaki.medtracker.domain.repository.MedicineRepository
import javax.inject.Inject

class DeleteMedicineUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
) {

    suspend operator fun invoke(id: Long) =
        medicineRepository.deleteMedicine(id)
}