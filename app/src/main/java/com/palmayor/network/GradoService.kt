package com.palmayor.network

import com.palmayor.models.response.GradoResponse
import retrofit2.Call
import retrofit2.http.GET

interface GradoService {

    @GET("Grados")
    fun getGrados(): Call<List<GradoResponse>>
}