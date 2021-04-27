package com.palmayor.models.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PersonaRequest(
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val dni: String,
    val sexo: String,
    val telefono: String?,
    val foto: String
):Serializable{
}