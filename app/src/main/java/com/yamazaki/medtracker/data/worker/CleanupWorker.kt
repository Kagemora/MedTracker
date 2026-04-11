package com.yamazaki.medtracker.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yamazaki.medtracker.domain.usecase.medicinelog.DeleteOldLogsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val deleteOldLogsUseCase: DeleteOldLogsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val ninetyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(90)
        deleteOldLogsUseCase(ninetyDaysAgo)
        return Result.success()
    }
}