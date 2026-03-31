package com.yamazaki.medtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yamazaki.medtracker.data.local.entity.MedicineLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineLogDao {

    /*
    логи за сегодня для Home экрана начало и конец дня передаём как timestamp
     */
    @Query("""
        SELECT * FROM medicines_log 
        WHERE scheduledAt >= :startOfDay AND scheduledAt < :endOfDay
        ORDER BY scheduledAt ASC
    """)
    fun getTodayLogs(startOfDay: Long, endOfDay: Long): Flow<List<MedicineLogEntity>>

    // логи за период для History экрана
    @Query("""
        SELECT * FROM medicines_log 
        WHERE scheduledAt >= :from AND scheduledAt < :to
        ORDER BY scheduledAt DESC
    """)
    fun getLogsByDateRange(from: Long, to: Long): Flow<List<MedicineLogEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLog(log: MedicineLogEntity): Long

    //обновляем только status и takenAt — остальные поля не трогам
    @Query("UPDATE medicines_log SET status = :status, takenAt = :takenAt WHERE id = :logId")
    suspend fun updateLogStatus(logId: Long, status: String, takenAt: Long?)

    /* удаляем логи старше указанной даты будет вызываться раз в неделю через WorkManager
    */
    @Query("DELETE FROM medicines_log WHERE scheduledAt < :olderThan")
    suspend fun deleteOldLogs(olderThan: Long)
}