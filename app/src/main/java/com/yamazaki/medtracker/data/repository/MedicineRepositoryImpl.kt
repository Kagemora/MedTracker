package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.data.local.dao.MedicineDao
import com.yamazaki.medtracker.data.mapper.toDb
import com.yamazaki.medtracker.data.mapper.toDomain
import com.yamazaki.medtracker.domain.model.Medicine
import com.yamazaki.medtracker.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
    private val medicineDao: MedicineDao
) : MedicineRepository {
    /** получить все активные лекарства */
    override fun getAllActiveMedicines(): Flow<List<Medicine>> {
        return medicineDao.getAllActiveMedicines().map { list ->
            list.map { it.toDomain() }
        }
    }

    /** получить лекарство по ID */
    override suspend fun getMedicineById(id: Long): Medicine? {
        return medicineDao.getMedicineById(id)?.toDomain()
    }

    /** добавить новое лекарство */
    override suspend fun insertMedicine(medicine: Medicine): Long {
        return medicineDao.insertMedicine(medicine.toDb())
    }

    /** обновить лекарство */
    override suspend fun updateMedicine(medicine: Medicine) {
        medicineDao.updateMedicine(medicine.toDb())
    }

    /** мягкое удаление ставим isActive = false */
    override suspend fun deleteMedicine(medicine: Long) {
        medicineDao.deleteMedicine(medicine)
    }
}