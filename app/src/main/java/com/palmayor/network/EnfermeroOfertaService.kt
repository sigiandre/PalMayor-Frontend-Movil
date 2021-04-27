package com.palmayor.network

import com.palmayor.models.response.EnfermeroOfertaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import com.palmayor.models.request.EnfermeroOfertaRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EnfermeroOfertaService {

    @POST("EnfermeroOfertas")
    fun postEnfermeroOferta(@Body body: EnfermeroOfertaRequest): Call<EnfermeroOfertaRequest>

    @GET("EnfermeroOfertas/enfermero/{correo}")
    fun getEnfermeroOfertasByCorreo(@Path("correo") correo: String) : Call<List<EnfermeroOfertaResponse>>

    @GET("EnfermeroOfertas/oferta/{id}")
    fun getEnfermeroOfertasByOfertaId(@Path("id") id: Int): Call<List<EnfermeroOfertaResponse>>
}