package com.yamazaki.medtracker.data.mapper

import com.yamazaki.medtracker.data.local.entity.MedicineEntity
import com.yamazaki.medtracker.data.local.entity.MedicineLogEntity
import com.yamazaki.medtracker.data.local.entity.ScheduleEntity
import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.model.Medicine
import com.yamazaki.medtracker.domain.model.MedicineLog
import com.yamazaki.medtracker.domain.model.Schedule

fun Medicine.toDb(): MedicineEntity {
    return MedicineEntity(
        id = id,
        name = name,
        dosage = dosage,
        unit = unit,
        color = color,
        notes = notes,
        isActive = isActive
    )
}

fun MedicineEntity.toDomain(): Medicine {
    return Medicine(
        id = id,
        name = name,
        dosage = dosage,
        unit = unit,
        color = color,
        notes = notes,
        isActive = isActive
    )
}

fun Schedule.toDb(): ScheduleEntity {
    return ScheduleEntity(
        id = id,
        medicineId = medicineId,
        medicineName = medicineName,
        hour = hour,
        minute = minute,
        days = days,
        isEnabled = isEnabled
    )
}

fun ScheduleEntity.toDomain(): Schedule {
    return Schedule(
        id = id,
        medicineId = medicineId,
        medicineName = medicineName,
        hour = hour,
        minute = minute,
        days = days,
        isEnabled = isEnabled
    )
}

fun MedicineLog.toDb(): MedicineLogEntity {
    return MedicineLogEntity(
        id = id,
        scheduleId = scheduleId,
        medicineName = medicineName,
        dosage = dosage,
        unit = unit,
        scheduledAt = scheduledAt,
        takenAt = takenAt,
        status = status.name
    )
}

fun MedicineLogEntity.toDomain(): MedicineLog {
    return MedicineLog(
        id = id,
        scheduleId = scheduleId ?: 0L, //устанавливаем 0 как "расписание удалено"
        medicineName = medicineName,
        dosage = dosage,
        unit = unit,
        scheduledAt = scheduledAt,
        takenAt = takenAt,
        status = LogStatus.valueOf(status)
    )
}