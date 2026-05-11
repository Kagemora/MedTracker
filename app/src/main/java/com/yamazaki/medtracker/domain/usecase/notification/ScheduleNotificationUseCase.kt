package com.yamazaki.medtracker.domain.usecase.notification

import com.yamazaki.medtracker.domain.model.Schedule
import com.yamazaki.medtracker.domain.repository.NotificationRepository
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(schedule: Schedule) =
        repository.schedule(schedule)
}