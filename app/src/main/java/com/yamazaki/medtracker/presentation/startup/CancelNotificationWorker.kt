package com.yamazaki.medtracker.presentation.startup

import androidx.work.WorkManager
import javax.inject.Inject


/**
 * UseCase для отмены запланированного уведомления.
 *
 * Вызывается когда пользователь удаляет расписание или отключает его.
 * Отменяет Worker по тегу "schedule_{id}" который был установлен
 * в [WorkerStartupManager].
 */
class CancelNotificationWorker @Inject constructor(
    private val workManager: WorkManager
) {
    operator fun invoke(scheduleId: Long) {
        workManager.cancelAllWorkByTag("schedule_$scheduleId")
    }
}