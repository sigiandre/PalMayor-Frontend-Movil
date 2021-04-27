package com.palmayor.network

import com.palmayor.models.request.ServicioRequest
import com.palmayor.models.response.ServicioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServicioService {
    @GET("Servicios/Familiar/correo/{param}")
    fun getServiciosByFamiliarCorreo(@Path("param") param: String) : Call<List<ServicioResponse>>

    @GET("Servicios/{id}")
    fun getServicioById(@Path("id") id: Int): Call<ServicioResponse>

    @GET("Servicios/Enfermero/correo/{correo}")
    fun getServicioByEnfermeroCorreo(@Path("correo") correo: String) : Call<List<ServicioResponse>>

    @POST("Servicios")
    fun postServicio(@Body body: ServicioRequest): Call<ServicioResponse>
}