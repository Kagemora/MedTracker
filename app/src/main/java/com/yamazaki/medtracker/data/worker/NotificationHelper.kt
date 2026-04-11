package com.yamazaki.medtracker.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.yamazaki.medtracker.data.reciever.ReminderReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager

    init {
        createChannel()
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Напоминания о лекарствах",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Уведомления о времени приёма лекарств"
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showMedicineReminder(
        logId: Long,
        medicineName: String,
        dosage: String,
        unit: String
    ) {
        val takenIntent = PendingIntent.getBroadcast(
            context,
            logId.toInt(),
            Intent(context, ReminderReceiver::class.java).apply {
                action = MedicineReminderWorker.ACTION_TAKEN
                putExtra("log_id", logId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val skippedIntent = PendingIntent.getBroadcast(
            context,
            (logId + 10000).toInt(),
            Intent(context, ReminderReceiver::class.java).apply {
                action = MedicineReminderWorker.ACTION_SKIPPED
                putExtra("log_id", logId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Время принять лекарство")
            .setContentText("$medicineName $dosage $unit")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(0, "Принял", takenIntent)
            .addAction(0, "Пропустил", skippedIntent)
            .build()

        notificationManager.notify(logId.toInt(), notification)
    }

    fun cancelNotification(logId: Long) {
        notificationManager.cancel(logId.toInt())
    }

    companion object {
        const val CHANNEL_ID = "medicine_reminders"
    }
}