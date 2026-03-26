package com.yamazaki.medtracker.domain.usecase.medicine

import com.yamazaki.medtracker.domain.repository.MedicineRepository
import javax.inject.Inject

class GetAllActiveMedicinesUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
) {

    operator fun invoke() =
        medicineRepository.getAllActiveMedicines()
}