package com.yamazaki.medtracker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrugResultDto(
    val openfda: OpenFdaDto = OpenFdaDto(),
    val purpose: List<String> = emptyList(),
    val warnings: List<String> = emptyList()
)