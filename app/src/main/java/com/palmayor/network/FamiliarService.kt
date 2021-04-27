package com.palmayor.network

import com.palmayor.models.request.FamiliarRequest
import com.palmayor.models.response.FamiliarResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FamiliarService {

    @POST("Familiares")
    suspend fun postFamiliar(@Body body: FamiliarRequest) : Response<FamiliarResponse>

    @GET("Familiares/correo/{param}")
    fun getFamiliar(@Path("param") param: String) : Call<FamiliarResponse>
}