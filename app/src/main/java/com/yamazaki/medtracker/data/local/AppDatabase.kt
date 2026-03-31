package com.yamazaki.medtracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yamazaki.medtracker.data.local.dao.MedicineDao
import com.yamazaki.medtracker.data.local.dao.MedicineLogDao
import com.yamazaki.medtracker.data.local.dao.ScheduleDao
import com.yamazaki.medtracker.data.local.entity.MedicineEntity
import com.yamazaki.medtracker.data.local.entity.MedicineLogEntity
import com.yamazaki.medtracker.data.local.entity.ScheduleEntity

@Database(
    entities = [
        MedicineEntity::class,
        MedicineLogEntity::class,
        ScheduleEntity::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
    abstract fun medicineLogDao(): MedicineLogDao
    abstract fun scheduleDao(): ScheduleDao
}