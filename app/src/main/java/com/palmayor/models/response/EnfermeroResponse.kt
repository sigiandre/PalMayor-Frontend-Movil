package com.palmayor.models.response

import com.google.gson.annotations.SerializedName

data class EnfermeroResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("colegiatura")
    val colegiatura: String,

    @SerializedName("universidad")
    val universidad: String,

    @SerializedName("experiencia")
    val experiencia: String,

    @SerializedName("personaId")
    val personaId: Int,

    @SerializedName("persona")
    val persona: PersonaResponse,

    @SerializedName("especialidad")
    val especialidad: EspecialidadResponse,

    @SerializedName("grado")
    val grado: GradoResponse
){
}