package com.yamazaki.medtracker.domain.usecase.notification

import com.yamazaki.medtracker.domain.repository.NotificationRepository
import javax.inject.Inject

class CancelNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(scheduleId: Long) =
        repository.cancel(scheduleId)
}