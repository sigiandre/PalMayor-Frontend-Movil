package com.palmayor.network

import com.palmayor.models.request.OfertaRequest
import com.palmayor.models.response.OfertaResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OfertasService {
    @GET("Ofertas/familiar/correo/{param}")
    fun getOfertasByEmail(@Path("param") param: String): Call<List<OfertaResponse>>

    @GET("Ofertas")
    fun getOfertas(): Call<List<OfertaResponse>>

    @GET("Ofertas/{param}")
    fun getOfertaById(@Path("param") param: Int): Call<OfertaResponse>

    @POST("Ofertas")
    suspend fun postOferta(@Body body: OfertaRequest): Response<OfertaResponse>
}