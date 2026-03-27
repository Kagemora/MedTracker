package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.model.MedicineLog
import com.yamazaki.medtracker.domain.repository.MedicineLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MedicineLogRepositoryImpl @Inject constructor(

) : MedicineLogRepository {
    /** логи на сегодня (главный экран Home) */
    override fun getTodayLogs(): Flow<List<MedicineLog>> {
        TODO("Not yet implemented")
    }

    /** история за период (экран History) */
    override fun getLogsByDateRange(
        from: Long,
        to: Long
    ): Flow<List<MedicineLog>> {
        TODO("Not yet implemented")
    }

    /** создать новый лог (обычно делает Worker) */
    override suspend fun insertLog(log: MedicineLog): Long {
        TODO("Not yet implemented")
    }

    /** отметить как принято / пропущено (кнопки в уведомлении и на экране) */
    override suspend fun updateLogStatus(
        logId: Long,
        status: LogStatus,
        takenAt: Long?
    ) {
        TODO("Not yet implemented")
    }

    /** очистка старых записей (будет запускаться периодически через WorkManager) */
    override suspend fun deleteOldLogs(olderThan: Long) {
        TODO("Not yet implemented")
    }
}