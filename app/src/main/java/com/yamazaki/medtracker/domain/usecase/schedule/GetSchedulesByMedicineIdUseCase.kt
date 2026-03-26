package com.yamazaki.medtracker.domain.usecase.schedule

import com.yamazaki.medtracker.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetSchedulesByMedicineIdUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {

    operator fun invoke(id: Long) =
        scheduleRepository.getSchedulesByMedicineId(id)
}