package com.yamazaki.medtracker.data.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yamazaki.medtracker.data.worker.RescheduleAllWorker

/**
 * Получает BOOT_COMPLETED и делегирует перепланирование в RescheduleAllWorker.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val request = OneTimeWorkRequestBuilder<RescheduleAllWorker>()
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "reschedule_on_boot",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}