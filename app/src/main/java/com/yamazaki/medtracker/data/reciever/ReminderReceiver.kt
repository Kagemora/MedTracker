package com.yamazaki.medtracker.data.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yamazaki.medtracker.data.worker.MedicineReminderWorker
import com.yamazaki.medtracker.data.worker.NotificationHelper
import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.usecase.medicinelog.UpdateLogStatusUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Обрабатывает нажатие кнопок "Принял" / "Пропустил" в уведомлении.
 *
 * Использует [goAsync] потому что [onReceive] синхронный —
 * без него Android может убить процесс до завершения корутины.
 */
@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateLogStatusUseCase: UpdateLogStatusUseCase

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val logId = intent.getLongExtra("log_id", -1L)
        if (logId == -1L) return

        val status = when (intent.action) {
            MedicineReminderWorker.ACTION_TAKEN   -> LogStatus.TAKEN
            MedicineReminderWorker.ACTION_SKIPPED -> LogStatus.SKIPPED
            else -> return
        }

        val takenAt = if (status == LogStatus.TAKEN) {
            System.currentTimeMillis()
        } else null

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                updateLogStatusUseCase(logId, status, takenAt)
            } finally {
                pendingResult.finish()
            }
        }

        notificationHelper.cancelNotification(logId)
    }
}
