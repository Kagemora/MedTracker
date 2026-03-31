package com.yamazaki.medtracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medicines_log",
    foreignKeys = [
        ForeignKey(
            entity = ScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("scheduleId")]
)
data class MedicineLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val scheduleId: Long?,
    val medicineName: String,
    val dosage: String,
    val unit: String,
    val scheduledAt: Long,
    val takenAt: Long? = null,
    val status: String
)

//при удалении расписания логи не удаляем onDelete = ForeignKey.SET_NULL