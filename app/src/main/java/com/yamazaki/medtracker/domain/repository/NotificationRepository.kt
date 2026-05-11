package com.yamazaki.medtracker.domain.repository

import com.yamazaki.medtracker.domain.model.Schedule

interface NotificationRepository {

    fun schedule(schedule: Schedule)
    fun cancel(scheduleId: Long)
}