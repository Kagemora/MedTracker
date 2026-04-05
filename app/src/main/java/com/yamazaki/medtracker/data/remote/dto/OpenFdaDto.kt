package com.yamazaki.medtracker.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFdaDto(
    @SerialName("brand_name")
    val brandName: List<String> = emptyList(),

    @SerialName("generic_name")
    val genericName: List<String> = emptyList(),

    @SerialName("manufacturer_name")
    val manufacturerName: List<String> = emptyList()
)
