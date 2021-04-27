package com.palmayor.network

import com.palmayor.models.request.RangoHoraRequest
import com.palmayor.models.response.RangoHoraResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RangoHorasService {
    @GET("RangoHoras")
    fun getRangoHoras(): Call<List<RangoHoraResponse>>

    @POST("RangoHoras")
    suspend fun postRangoHoras(@Body body: RangoHoraRequest) : Response<RangoHoraResponse>
}