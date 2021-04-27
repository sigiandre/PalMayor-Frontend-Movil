package com.palmayor.models.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


data class PersonaResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellidos")
    val apellidos: String,

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String,

    @SerializedName("dni")
    val dni: String,

    @SerializedName("sexo")
    val sexo: String,

    @SerializedName("telefono")
    val telefono: String?,

    @SerializedName("foto")
    val foto: String
):Serializable{}