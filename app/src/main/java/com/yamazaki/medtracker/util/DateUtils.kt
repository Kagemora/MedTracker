package com.yamazaki.medtracker.util

import com.yamazaki.medtracker.util.DateUtils.calculateDelay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

object DateUtils {

    /**
     * Возвращает диапазон сегодняшнего дня в миллисекундах.
     *
     * @return Pair где first = начало дня (00:00:00.000), second = начало следующего дня (00:00:00.000)
     *
     * Используется в [com.yamazaki.medtracker.domain.repository.MedicineLogRepository.getTodayLogs] для фильтрации логов за сегодня:
     * WHERE scheduledAt >= startOfDay AND scheduledAt < endOfDay
     */
    fun getTodayRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000L
        return Pair(startOfDay, endOfDay)
    }

    /**
     * Вычисляет задержку в миллисекундах до ближайшего срабатывания по времени [hour]:[minute].
     *
     * Используется при ПЕРВОМ планировании Worker-а из [com.yamazaki.medtracker.presentation.startup.WorkerStartupManager]
     * и при перепланировании в [com.yamazaki.medtracker.data.reciever.BootReceiver] после перезагрузки устройства.
     *
     * Если указанное время уже прошло сегодня — возвращает задержку до завтра.
     *
     * Пример: сейчас 14:00, hour=8, minute=0 → вернёт задержку до завтра 08:00
     *
     * @param hour часы в формате 0-23
     * @param minute минуты в формате 0-59
     * @return задержка в миллисекундах
     */
    fun calculateDelay(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val trigger = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        // если время уже прошло сегодня — переносим на завтра
        if (trigger.before(now)) {
            trigger.add(Calendar.DAY_OF_YEAR, 1)
        }
        return trigger.timeInMillis - now.timeInMillis
    }

    /**
     * Вычисляет задержку до следующего срабатывания с учётом дней недели.
     *
     * Используется в [com.yamazaki.medtracker.data.worker.MedicineReminderWorker.scheduleNext] для перепланирования
     * после каждого срабатывания. В отличие от [calculateDelay] учитывает [days] —
     * Worker не должен планироваться на день которого нет в расписании.
     *
     * Поиск начинается с ЗАВТРА (daysAhead = 1) — сегодня Worker уже сработал.
     *
     * Конвертация дней недели:
     * Calendar: 1=вс, 2=пн, 3=вт, 4=ср, 5=чт, 6=пт, 7=сб
     * Наш формат: 1=пн, 2=вт, 3=ср, 4=чт, 5=пт, 6=сб, 7=вс
     * Формула: if (calDay == 1) 7 else calDay - 1
     *
     * Пример: сегодня пятница, days=[1,2,3,4,5] (будни)
     * → следующий подходящий день — понедельник
     * → вернёт задержку до понедельника в миллисекундах
     *
     * @param hour часы в формате 0-23
     * @param minute минуты в формате 0-59
     * @param days список дней недели в формате 1=пн...7=вс
     * @return задержка в миллисекундах, или -1 если [days] пустой
     */
    fun calculateNextDelay(hour: Int, minute: Int, days: List<Int>): Long {
        if (days.isEmpty()) return -1L

        val now = Calendar.getInstance()

        for (daysAhead in 1..7) {
            val candidate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, daysAhead)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            // конвертируем Calendar день в наш формат
            val calDay = candidate.get(Calendar.DAY_OF_WEEK)
            val ourDay = if (calDay == 1) 7 else calDay - 1

            if (ourDay in days) {
                return candidate.timeInMillis - now.timeInMillis
            }
        }
        // все 7 дней проверены — подходящего не нашли (days некорректный)
        return -1L
    }

    fun formatTime(timestamp: Long, is24Hour: Boolean = true): String {
        val pattern = if (is24Hour) "HH:mm" else "hh:mm a"
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern(pattern))
    }

    fun formatScheduleTime(hour: Int, minute: Int, is24Hour: Boolean = true): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return formatTime(calendar.timeInMillis, is24Hour)
    }
}