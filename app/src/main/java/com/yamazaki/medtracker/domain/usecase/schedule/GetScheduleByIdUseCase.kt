package com.yamazaki.medtracker.domain.usecase.schedule

import com.yamazaki.medtracker.domain.model.Schedule
import com.yamazaki.medtracker.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetScheduleByIdUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {

    suspend operator fun invoke(id: Long): Schedule? {
        return scheduleRepository.getScheduleById(id)
    }
}