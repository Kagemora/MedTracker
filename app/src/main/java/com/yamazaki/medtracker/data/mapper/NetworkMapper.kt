package com.yamazaki.medtracker.data.mapper

import com.yamazaki.medtracker.data.remote.dto.DrugResultDto
import com.yamazaki.medtracker.domain.model.DrugInfo

fun DrugResultDto.toDomain(): DrugInfo {
    return DrugInfo(
        id = id ?: "${openfda.brandName.firstOrNull()}_${openfda.genericName.firstOrNull()}",
        brandName = openfda.brandName.firstOrNull() ?: "Неизвестно",
        genericName = openfda.genericName.firstOrNull() ?: "Неизвестно",
        manufacturer = openfda.manufacturerName.firstOrNull() ?: "Неизвестно",
        purpose = purpose.firstOrNull() ?: "Нет описания",
        warnings = warnings.firstOrNull() ?: "Нет данных"
    )
}