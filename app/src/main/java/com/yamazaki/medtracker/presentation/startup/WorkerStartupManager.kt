package com.yamazaki.medtracker.presentation.startup

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.yamazaki.medtracker.data.worker.MedicineReminderWorker
import com.yamazaki.medtracker.domain.model.Schedule
import com.yamazaki.medtracker.util.DateUtils
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * UseCase для планирования уведомления о приёме лекарства через WorkManager.
 *
 * Вызывается из ViewModel когда пользователь создаёт или обновляет расписание.
 * Использует [ExistingWorkPolicy.REPLACE] — если уведомление уже запланировано
 * для этого расписания, старое отменяется и создаётся новое с актуальным временем.
 *
 * Уникальное имя задачи "schedule_{id}" гарантирует что для одного расписания
 * всегда существует только один запланированный Worker.
 */
class WorkerStartupManager @Inject constructor(
    private val workManager: WorkManager
) {
    operator fun invoke(schedule: Schedule) {
        // вычисляем задержку до ближайшего срабатывания
        val delay = DateUtils.calculateDelay(schedule.hour, schedule.minute)

        // абсолютное время срабатывания — передаём в Worker
        // чтобы он записал точное запланированное время в MedicineLog.scheduledAt
        val nextTime = System.currentTimeMillis() + delay
        val request = OneTimeWorkRequestBuilder<MedicineReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    MedicineReminderWorker.KEY_SCHEDULE_ID to schedule.id,
                    MedicineReminderWorker.KEY_SCHEDULED_TIME to nextTime
                )
            )
            // тег для возможности отмены всех Workers конкретного расписания
            .addTag("schedule_${schedule.id}")
            .build()

        workManager.enqueueUniqueWork(
            "schedule_${schedule.id}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}