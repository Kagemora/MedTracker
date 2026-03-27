package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.domain.model.DrugInfo
import com.yamazaki.medtracker.domain.repository.DrugSearchRepository
import javax.inject.Inject

class DrugSearchRepositoryImpl @Inject constructor(

) : DrugSearchRepository {
    /** поиск лекарства через OpenFDA */
    override suspend fun searchDrug(query: String): List<DrugInfo> {
        TODO("Not yet implemented")
    }
}