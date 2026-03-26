package com.yamazaki.medtracker.domain.usecase.schedule

import com.yamazaki.medtracker.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetAllActiveSchedulesUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {

    operator fun invoke() =
        scheduleRepository.getAllActiveSchedules()
}