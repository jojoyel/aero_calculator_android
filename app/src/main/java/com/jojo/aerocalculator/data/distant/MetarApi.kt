package com.jojo.aerocalculator.data.distant

import retrofit2.http.GET
import retrofit2.http.Path

interface MetarApi {

    @GET("metar/{ICAO}")
    suspend fun metar(@Path("ICAO") icao: String): MetarResult
}


data class MetarResult(
    val data: List<String>,
    val results: Int
)