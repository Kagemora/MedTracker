package com.yamazaki.medtracker.domain.repository

import com.yamazaki.medtracker.domain.model.DrugInfo

interface DrugSearchRepository {

    /** поиск лекарства через OpenFDA */
    suspend fun searchDrug(query: String): List<DrugInfo>
}