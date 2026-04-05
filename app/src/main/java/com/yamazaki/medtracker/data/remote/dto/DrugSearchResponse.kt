package com.yamazaki.medtracker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrugSearchResponse(
    val results: List<DrugResultDto> = emptyList()
)
