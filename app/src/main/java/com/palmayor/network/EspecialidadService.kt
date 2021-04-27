package com.palmayor.network

import com.palmayor.models.response.EspecialidadResponse
import retrofit2.Call
import retrofit2.http.GET

interface EspecialidadService {

    @GET("Especialidades")
    fun getEspecialidades(): Call<List<EspecialidadResponse>>
}