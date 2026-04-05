package com.yamazaki.medtracker.data.remote.api

import com.yamazaki.medtracker.data.remote.dto.DrugSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFdaApi {

    @GET("drug/label.json")
    suspend fun searchDrug(
        @Query("search") query: String,
        @Query("limit") limit: Int = 10
    ): DrugSearchResponse

    companion object {
        // формируем строку запроса: openfda.brand_name:"ibuprofen"
        fun buildQuery(name: String): String =
            """openfda.brand_name:"$name""""
    }
}