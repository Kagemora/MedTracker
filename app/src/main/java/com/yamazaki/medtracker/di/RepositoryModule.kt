package com.yamazaki.medtracker.di

import com.yamazaki.medtracker.data.repository.DrugSearchRepositoryImpl
import com.yamazaki.medtracker.data.repository.MedicineLogRepositoryImpl
import com.yamazaki.medtracker.data.repository.MedicineRepositoryImpl
import com.yamazaki.medtracker.data.repository.ScheduleRepositoryImpl
import com.yamazaki.medtracker.data.repository.SettingsRepositoryImpl
import com.yamazaki.medtracker.domain.repository.DrugSearchRepository
import com.yamazaki.medtracker.domain.repository.MedicineLogRepository
import com.yamazaki.medtracker.domain.repository.MedicineRepository
import com.yamazaki.medtracker.domain.repository.ScheduleRepository
import com.yamazaki.medtracker.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindDrugSearchRepository(drugSearchRepositoryImpl: DrugSearchRepositoryImpl): DrugSearchRepository

    @Singleton
    @Binds
    fun bindMedicineLogRepository(medicineLogRepositoryImpl: MedicineLogRepositoryImpl): MedicineLogRepository

    @Singleton
    @Binds
    fun bindMedicineRepository(medicineRepositoryImpl: MedicineRepositoryImpl): MedicineRepository

    @Singleton
    @Binds
    fun bindScheduleRepository(scheduleRepositoryImpl: ScheduleRepositoryImpl): ScheduleRepository


    @Singleton
    @Binds
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}