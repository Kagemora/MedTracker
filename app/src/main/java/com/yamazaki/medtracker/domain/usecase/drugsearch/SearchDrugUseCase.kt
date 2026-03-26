package com.yamazaki.medtracker.domain.usecase.drugsearch

import com.yamazaki.medtracker.domain.model.DrugInfo
import com.yamazaki.medtracker.domain.repository.DrugSearchRepository
import javax.inject.Inject

class SearchDrugUseCase @Inject constructor(
    private val drugSearchRepository: DrugSearchRepository
) {

    suspend operator fun invoke(query: String): List<DrugInfo> =
        drugSearchRepository.searchDrug(query)
}