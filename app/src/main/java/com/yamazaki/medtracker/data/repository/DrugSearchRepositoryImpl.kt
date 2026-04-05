package com.yamazaki.medtracker.data.repository

import com.yamazaki.medtracker.data.mapper.toDomain
import com.yamazaki.medtracker.data.remote.api.OpenFdaApi
import com.yamazaki.medtracker.domain.model.DrugInfo
import com.yamazaki.medtracker.domain.repository.DrugSearchRepository
import javax.inject.Inject

class DrugSearchRepositoryImpl @Inject constructor(
    private val api: OpenFdaApi
) : DrugSearchRepository {
    /** поиск лекарства через OpenFDA */
    override suspend fun searchDrug(query: String): List<DrugInfo> {
        return try {
            api.searchDrug(OpenFdaApi.buildQuery(query))
                .results
                .map { it.toDomain() }
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) emptyList()
            else throw e
        }
    }
}