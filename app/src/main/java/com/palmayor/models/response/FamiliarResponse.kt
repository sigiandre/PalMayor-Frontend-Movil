package com.palmayor.models.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class FamiliarResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("usuarioId")
    val usuarioId: Int,

    @SerializedName("personaId")
    val personaId: Int,

    @SerializedName("persona")
    val persona: PersonaResponse?,

    @SerializedName("usuario")
    val usuario: UsuarioResponse?,

    @SerializedName("ancianos")
    val ancianos: List<AncianoResponse>?
){
}