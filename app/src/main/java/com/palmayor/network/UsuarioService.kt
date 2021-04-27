package com.palmayor.network

import com.palmayor.models.request.UsuarioRequest
import com.palmayor.models.response.UsuarioResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {

    @POST("Usuarios")
    suspend fun postUsuario(@Body body:UsuarioRequest) : Response<UsuarioResponse>

    @POST("Usuarios/login")
    fun loginUsuario(@Body body:UsuarioRequest) : Call<UsuarioResponse>
}