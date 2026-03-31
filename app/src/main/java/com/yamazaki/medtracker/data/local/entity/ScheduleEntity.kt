package com.yamazaki.medtracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule",
    foreignKeys = [
        ForeignKey(
        entity = MedicineEntity::class,
        parentColumns = ["id"],
        childColumns = ["medicineId"],
        onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicineId")]
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val medicineId: Long,
    val medicineName: String,
    val hour: Int,
    val minute: Int,
    val days: List<Int>,
    val isEnabled: Boolean
)

//при удалении лекарства удаляем и расписание