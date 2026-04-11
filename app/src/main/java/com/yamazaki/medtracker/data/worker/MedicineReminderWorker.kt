package com.yamazaki.medtracker.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.yamazaki.medtracker.data.worker.MedicineReminderWorker.Companion.KEY_SCHEDULED_TIME
import com.yamazaki.medtracker.data.worker.MedicineReminderWorker.Companion.KEY_SCHEDULE_ID
import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.model.MedicineLog
import com.yamazaki.medtracker.domain.model.Schedule
import com.yamazaki.medtracker.domain.usecase.medicine.GetMedicineByIdUseCase
import com.yamazaki.medtracker.domain.usecase.medicinelog.InsertLogUseCase
import com.yamazaki.medtracker.domain.usecase.schedule.GetScheduleByIdUseCase
import com.yamazaki.medtracker.util.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit


/**
 * Worker для показа напоминания о приёме лекарства.
 *
 * Срабатывает в запланированное время, создаёт [MedicineLog] со статусом [LogStatus.PENDING]
 * и показывает уведомление с кнопками "Принял" / "Пропустил".
 *
 * После выполнения планирует следующее срабатывание через [scheduleNext]
 * с учётом дней недели из [Schedule.days].
 *
 * Входные данные (inputData):
 * - [KEY_SCHEDULE_ID] — id расписания
 * - [KEY_SCHEDULED_TIME] — точное запланированное время в миллисекундах
 */
@HiltWorker
class MedicineReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val insertLogUseCase: InsertLogUseCase,
    private val notificationHelper: NotificationHelper,
    private val getMedicineByIdUseCase: GetMedicineByIdUseCase,
    private val getScheduleByIdUseCase: GetScheduleByIdUseCase
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_SCHEDULE_ID = "schedule_id"
        const val KEY_SCHEDULED_TIME = "scheduled_time"
        const val ACTION_TAKEN = "com.yamazaki.medtracker.ACTION_TAKEN"
        const val ACTION_SKIPPED = "com.yamazaki.medtracker.ACTION_SKIPPED"
    }

    override suspend fun doWork(): Result {
        val scheduleId = inputData.getLong(KEY_SCHEDULE_ID, -1L)
        val scheduledTime = inputData.getLong(KEY_SCHEDULED_TIME, -1L)
        if (scheduleId == -1L || scheduledTime == -1L) return Result.failure()

        val schedule = getScheduleByIdUseCase(scheduleId) ?: return Result.failure()
        val medicine = getMedicineByIdUseCase(schedule.medicineId) ?: return Result.failure()

        val logId = insertLogUseCase(
            MedicineLog(
                scheduleId = scheduleId,
                medicineName = medicine.name,
                dosage = medicine.dosage,
                unit = medicine.unit,
                scheduledAt = scheduledTime,
                takenAt = null,
                status = LogStatus.PENDING
            )
        )

        notificationHelper.showMedicineReminder(
            logId,
            medicine.name,
            medicine.dosage,
            medicine.unit
        )

        scheduleNext(schedule)

        return Result.success()
    }

    /**
     * Планирует следующее срабатывание Worker-а.
     * Ищет ближайший подходящий день из [Schedule.days] начиная с завтра.
     * Ничего не делает если [Schedule.isEnabled] = false или days пустой.
     */
    private fun scheduleNext(schedule: Schedule) {
        if (!schedule.isEnabled) return

        val delay = DateUtils.calculateNextDelay(
            hour = schedule.hour,
            minute = schedule.minute,
            days = schedule.days
        )

        if (delay <= 0) return

        val nextTime = System.currentTimeMillis() + delay

        val request = OneTimeWorkRequestBuilder<MedicineReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    KEY_SCHEDULE_ID to schedule.id,
                    KEY_SCHEDULED_TIME to nextTime
                )
            )
            .addTag("schedule_${schedule.id}")
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "schedule_${schedule.id}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
