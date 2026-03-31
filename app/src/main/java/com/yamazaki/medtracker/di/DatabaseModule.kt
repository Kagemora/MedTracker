package com.yamazaki.medtracker.di

import android.content.Context
import androidx.room.Room
import com.yamazaki.medtracker.data.local.AppDatabase
import com.yamazaki.medtracker.data.local.dao.MedicineDao
import com.yamazaki.medtracker.data.local.dao.MedicineLogDao
import com.yamazaki.medtracker.data.local.dao.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMedicineDao(database: AppDatabase): MedicineDao = database.medicineDao()

    @Provides
    @Singleton
    fun provideMedicineLogDao(database: AppDatabase): MedicineLogDao = database.medicineLogDao()

    @Provides
    @Singleton
    fun provideScheduleDao(database: AppDatabase): ScheduleDao = database.scheduleDao()


}