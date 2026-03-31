package com.yamazaki.medtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yamazaki.medtracker.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule WHERE medicineId = :medicineId")
    fun getSchedulesByMedicineId(medicineId: Long): Flow<List<ScheduleEntity>>

    /*
     нужно WorkManager при перезапуске устройства — чтобы перепланировать все активные уведомления
     */
    @Query("SELECT * FROM schedule WHERE isEnabled = 1")
    fun getAllActiveSchedules(): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Update
    suspend fun updateSchedule(schedule: ScheduleEntity)

    /*
    удаляем одно расписанеи
    */
    @Query("DELETE FROM schedule WHERE id = :scheduleId")
    suspend fun deleteSchedule(scheduleId: Long)

    /*
        удаление всех расписаний лекарства при мягком удаление
    */
    @Query("DELETE FROM schedule WHERE medicineId = :medicineId")
    suspend fun deleteSchedulesByMedicineId(medicineId: Long)
}