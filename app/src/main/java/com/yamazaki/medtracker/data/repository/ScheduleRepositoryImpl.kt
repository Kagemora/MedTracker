package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.domain.model.Schedule
import com.yamazaki.medtracker.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(

) : ScheduleRepository {
    /** расписания конкретного лекарства */
    override fun getSchedulesByMedicineId(medicineId: Long): Flow<List<Schedule>> {
        TODO("Not yet implemented")
    }

    /** все активные расписания (нужно для WorkManager при перезапуске) */
    override fun getAllActiveSchedules(): Flow<List<Schedule>> {
        TODO("Not yet implemented")
    }

    /** добавить расписание */
    override suspend fun insertSchedule(schedule: Schedule): Long {
        TODO("Not yet implemented")
    }

    /** обновить расписание */
    override suspend fun updateSchedule(schedule: Schedule) {
        TODO("Not yet implemented")
    }

    /** удалить одно расписание */
    override suspend fun deleteSchedule(scheduleId: Long) {
        TODO("Not yet implemented")
    }

    /** удалить все расписания лекарства (при удалении Medicine) */
    override suspend fun deleteSchedulesByMedicineId(medicineId: Long) {
        TODO("Not yet implemented")
    }
}