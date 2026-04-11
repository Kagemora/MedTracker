package com.yamazaki.medtracker.data.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.yamazaki.medtracker.data.worker.MedicineReminderWorker
import com.yamazaki.medtracker.domain.usecase.schedule.GetAllActiveSchedulesUseCase
import com.yamazaki.medtracker.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * BroadcastReceiver для перепланирования уведомлений после перезагрузки устройства.
 *
 * Проблема: WorkManager не сохраняет запланированные задачи после ребута.
 * Решение: слушаем [Intent.ACTION_BOOT_COMPLETED] и заново планируем
 * [MedicineReminderWorker] для всех активных расписаний.
 *
 * Требует разрешения RECEIVE_BOOT_COMPLETED в манифесте.
 * exported=true — Intent приходит от системы Android, не от нашего приложения.
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getAllActiveSchedulesUseCase: GetAllActiveSchedulesUseCase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val schedules = getAllActiveSchedulesUseCase().first()

                schedules.forEach { schedule ->
                    val delay = DateUtils.calculateDelay(
                        schedule.hour,
                        schedule.minute
                    )
                    // абсолютное время срабатывания для MedicineLog.scheduledAt
                    val nextTime = System.currentTimeMillis() + delay

                    val request = OneTimeWorkRequestBuilder<MedicineReminderWorker>()
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(
                            workDataOf(
                                MedicineReminderWorker.KEY_SCHEDULE_ID to schedule.id,
                                MedicineReminderWorker.KEY_SCHEDULED_TIME to nextTime
                            )
                        )
                        .addTag("schedule_${schedule.id}")
                        .build()

                    WorkManager.getInstance(context).enqueueUniqueWork(
                        "schedule_${schedule.id}",
                        ExistingWorkPolicy.REPLACE,
                        request
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}