package com.palmayor.network

import com.palmayor.models.request.EnfermeroRequest
import com.palmayor.models.response.EnfermeroResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.security.auth.callback.Callback

interface EnfermeroService {

    @GET("Enfermeros/correo/{correo}")
    fun GetEnfermeroByCorreo(@Path("correo") correo: String): Call<EnfermeroResponse>

    @POST("Enfermeros")
    suspend fun postEnfermero(@Body body: EnfermeroRequest): Response<EnfermeroResponse>
}