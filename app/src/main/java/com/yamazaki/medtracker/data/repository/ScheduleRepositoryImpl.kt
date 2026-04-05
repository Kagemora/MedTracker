package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.data.local.dao.ScheduleDao
import com.yamazaki.medtracker.data.mapper.toDb
import com.yamazaki.medtracker.data.mapper.toDomain
import com.yamazaki.medtracker.domain.model.Schedule
import com.yamazaki.medtracker.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {
    /** расписания конкретного лекарства */
    override fun getSchedulesByMedicineId(medicineId: Long): Flow<List<Schedule>> {
        return scheduleDao.getSchedulesByMedicineId(medicineId).map { list ->
            list.map { it.toDomain() }
        }
    }

    /** все активные расписания (нужно для WorkManager при перезапуске) */
    override fun getAllActiveSchedules(): Flow<List<Schedule>> {
        return scheduleDao.getAllActiveSchedules().map { list ->
            list.map { it.toDomain() }
        }
    }

    /** добавить расписание */
    override suspend fun insertSchedule(schedule: Schedule): Long {
        return scheduleDao.insertSchedule(schedule.toDb())
    }

    /** обновить расписание */
    override suspend fun updateSchedule(schedule: Schedule) {
        scheduleDao.updateSchedule(schedule.toDb())
    }

    /** удалить одно расписание */
    override suspend fun deleteSchedule(scheduleId: Long) {
        scheduleDao.deleteSchedule(scheduleId)
    }

    /** удалить все расписания лекарства (при удалении Medicine) */
    override suspend fun deleteSchedulesByMedicineId(medicineId: Long) {
        scheduleDao.deleteSchedulesByMedicineId(medicineId)
    }

}