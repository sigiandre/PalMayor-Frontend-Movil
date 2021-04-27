package com.palmayor.network

import com.palmayor.models.request.AncianoRequest
import com.palmayor.models.response.AncianoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AncianoService {

    @GET("Ancianos/familiar/correo/{correo}")
    fun getAncianos(@Path("correo") correo: String):Call<List<AncianoResponse>>

    @POST("Ancianos")
    fun postAnciano(@Body body:AncianoRequest) : Call<AncianoResponse>
}