package com.yamazaki.medtracker.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.yamazaki.medtracker.domain.usecase.schedule.GetAllActiveSchedulesUseCase
import com.yamazaki.medtracker.util.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class RescheduleAllWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getAllActiveSchedulesUseCase: GetAllActiveSchedulesUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val schedules = getAllActiveSchedulesUseCase().first()

        schedules.forEach { schedule ->
            val delay = DateUtils.calculateDelay(schedule.hour, schedule.minute)
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

            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                "schedule_${schedule.id}",
                ExistingWorkPolicy.REPLACE,
                request
            )
        }

        return Result.success()
    }
}