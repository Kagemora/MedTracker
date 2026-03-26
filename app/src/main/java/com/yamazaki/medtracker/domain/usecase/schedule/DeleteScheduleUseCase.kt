package com.yamazaki.medtracker.domain.usecase.schedule

import com.yamazaki.medtracker.domain.repository.ScheduleRepository
import javax.inject.Inject

class DeleteScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {

    suspend operator fun invoke(id: Long) =
        scheduleRepository.deleteSchedule(id)
}