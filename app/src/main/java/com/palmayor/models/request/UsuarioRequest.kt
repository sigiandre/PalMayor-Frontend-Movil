package com.palmayor.models.request

import java.io.Serializable

data class UsuarioRequest(
    val correo: String,
    val contrasenya:String,
    val rolId: Int
):Serializable{
}