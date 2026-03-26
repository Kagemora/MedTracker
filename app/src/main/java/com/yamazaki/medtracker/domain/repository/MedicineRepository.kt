package com.yamazaki.medtracker.domain.repository

import com.yamazaki.medtracker.domain.model.Medicine
import kotlinx.coroutines.flow.Flow

//репозиторий для основной работы с лекарствами
interface MedicineRepository {

    /** получить все активные лекарства */
    fun getAllActiveMedicines(): Flow<List<Medicine>>

    /** получить лекарство по ID */
    suspend fun getMedicineById(id: Long): Medicine?

    /** добавить новое лекарство */
    suspend fun insertMedicine(medicine: Medicine): Long

    /** обновить лекарство */
    suspend fun updateMedicine(medicine: Medicine)

    /** мягкое удаление ставим isActive = false */
    suspend fun deleteMedicine(medicine: Long)
}