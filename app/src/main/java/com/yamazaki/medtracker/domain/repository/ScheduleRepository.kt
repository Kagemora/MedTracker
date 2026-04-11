package com.yamazaki.medtracker.domain.repository

import com.yamazaki.medtracker.domain.model.Schedule
import kotlinx.coroutines.flow.Flow

//репозиторий управления временем и днями приёма
interface ScheduleRepository {

    /** расписания конкретного лекарства */
    fun getSchedulesByMedicineId(medicineId: Long): Flow<List<Schedule>>

    /** все активные расписания (нужно для WorkManager при перезапуске) */
    fun getAllActiveSchedules(): Flow<List<Schedule>>

    /** добавить расписание */
    suspend fun insertSchedule(schedule: Schedule): Long

    /** обновить расписание */
    suspend fun updateSchedule(schedule: Schedule)

    /** удалить одно расписание */
    suspend fun deleteSchedule(scheduleId: Long)

    /** удалить все расписания лекарства (при удалении Medicine) */
    suspend fun deleteSchedulesByMedicineId(medicineId: Long)

    /** получение расписания*/
    suspend fun getScheduleById(id: Long): Schedule?
}