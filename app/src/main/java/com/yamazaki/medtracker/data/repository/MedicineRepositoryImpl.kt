package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.domain.model.Medicine
import com.yamazaki.medtracker.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(

): MedicineRepository {
    /** получить все активные лекарства */
    override fun getAllActiveMedicines(): Flow<List<Medicine>> {
        TODO("Not yet implemented")
    }

    /** получить лекарство по ID */
    override suspend fun getMedicineById(id: Long): Medicine? {
        TODO("Not yet implemented")
    }

    /** добавить новое лекарство */
    override suspend fun insertMedicine(medicine: Medicine): Long {
        TODO("Not yet implemented")
    }

    /** обновить лекарство */
    override suspend fun updateMedicine(medicine: Medicine) {
        TODO("Not yet implemented")
    }

    /** мягкое удаление ставим isActive = false */
    override suspend fun deleteMedicine(medicine: Long) {
        TODO("Not yet implemented")
    }
}