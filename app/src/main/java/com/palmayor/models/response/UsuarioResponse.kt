package com.palmayor.models.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class UsuarioResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("correo")
    val correo: String,

    @SerializedName("rolId")
    val rolId: Int,

    @SerializedName("rol")
    var rol: RolResponse
){
}