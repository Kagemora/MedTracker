package com.yamazaki.medtracker.domain.repository

import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.model.MedicineLog
import kotlinx.coroutines.flow.Flow

//репозиторий логов по приему лекарств
interface MedicineLogRepository {

    /** логи на сегодня (главный экран Home) */
    fun getTodayLogs(): Flow<List<MedicineLog>>

    /** история за период (экран History) */
    fun getLogsByDateRange(from: Long, to: Long): Flow<List<MedicineLog>>

    /** создать новый лог (обычно делает Worker) */
    suspend fun insertLog(log: MedicineLog): Long

    /** отметить как принято / пропущено (кнопки в уведомлении и на экране) */
    suspend fun updateLogStatus(
        logId: Long,
        status: LogStatus,
        takenAt: Long?
    )

    /** очистка старых записей (будет запускаться периодически через WorkManager) */
    suspend fun deleteOldLogs(olderThan: Long)
}