package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.data.local.dao.MedicineLogDao
import com.yamazaki.medtracker.data.mapper.toDb
import com.yamazaki.medtracker.data.mapper.toDomain
import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.model.MedicineLog
import com.yamazaki.medtracker.domain.repository.MedicineLogRepository
import com.yamazaki.medtracker.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedicineLogRepositoryImpl @Inject constructor(
    private val medicineDao: MedicineLogDao
) : MedicineLogRepository {
    /** логи на сегодня (главный экран Home) */
    override fun getTodayLogs(): Flow<List<MedicineLog>> {
        val (startOfDay, endOfDay) = DateUtils.getTodayRange()
        return medicineDao.getTodayLogs(startOfDay, endOfDay)
            .map { list -> list.map { it.toDomain() } }
    }

    /** история за период (экран History) */
    override fun getLogsByDateRange(
        from: Long,
        to: Long
    ): Flow<List<MedicineLog>> {
        return medicineDao.getLogsByDateRange(from, to).map { list ->
            list.map { it.toDomain() }
        }

    }

    /** создать новый лог (обычно делает Worker) */
    override suspend fun insertLog(log: MedicineLog): Long {
        return medicineDao.insertLog(log.toDb())
    }

    /** отметить как принято / пропущено (кнопки в уведомлении и на экране) */
    override suspend fun updateLogStatus(
        logId: Long,
        status: LogStatus,
        takenAt: Long?
    ) {
        medicineDao.updateLogStatus(logId, status.name, takenAt)
    }

    /** очистка старых записей (будет запускаться периодически через WorkManager) */
    override suspend fun deleteOldLogs(olderThan: Long) {
        medicineDao.deleteOldLogs(olderThan)
    }
}