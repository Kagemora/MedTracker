package com.yamazaki.medtracker.domain.usecase

import com.yamazaki.medtracker.domain.model.DrugInfo
import com.yamazaki.medtracker.domain.model.Medicine
import com.yamazaki.medtracker.domain.model.Schedule
import com.yamazaki.medtracker.domain.usecase.medicine.InsertMedicineUseCase
import com.yamazaki.medtracker.domain.usecase.notification.ScheduleNotificationUseCase
import com.yamazaki.medtracker.domain.usecase.schedule.InsertScheduleUseCase
import javax.inject.Inject

class CreateMedicineWithScheduleUseCase @Inject constructor(
    private val insertMedicineUseCase: InsertMedicineUseCase,
    private val insertScheduleUseCase: InsertScheduleUseCase,
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase
) {
    suspend operator fun invoke(
        drug: DrugInfo,
        dosage: String,
        unit: String,
        hour: Int,
        minute: Int,
        days: List<Int>
    ) {
        val medicine = Medicine(
            name = drug.brandName,
            dosage = dosage,
            unit = unit,
            color = 0xFF6650A4.toInt(),
            notes = drug.purpose,
            isActive = true
        )
        val medicineId = insertMedicineUseCase(medicine)

        val schedule = Schedule(
            medicineId = medicineId,
            medicineName = drug.brandName,
            hour = hour,
            minute = minute,
            days = days,
            isEnabled = true
        )
        val scheduleId = insertScheduleUseCase(schedule)

        scheduleNotificationUseCase(schedule.copy(id = scheduleId))
    }
}