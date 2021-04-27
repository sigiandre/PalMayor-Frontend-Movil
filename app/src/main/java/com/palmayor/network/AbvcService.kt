package com.palmayor.network

import com.palmayor.models.response.ABVCResponse
import retrofit2.Call
import retrofit2.http.GET

interface AbvcService {
    @GET("ABVCs")
    fun getAbvcs(): Call<List<ABVCResponse>>
}