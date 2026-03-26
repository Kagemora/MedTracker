package com.yamazaki.medtracker.domain.usecase.medicine

import com.yamazaki.medtracker.domain.model.Medicine
import com.yamazaki.medtracker.domain.repository.MedicineRepository
import javax.inject.Inject

class UpdateMedicineUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
) {

    suspend operator fun invoke(medicine: Medicine) =
        medicineRepository.updateMedicine(medicine)
}