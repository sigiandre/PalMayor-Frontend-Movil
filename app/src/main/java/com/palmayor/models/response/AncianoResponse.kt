package com.palmayor.models.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AncianoResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("familiarId")
    val familiarId: Int,

    @SerializedName("personaId")
    val personaId: Int,

    @SerializedName("persona")
    val persona: PersonaResponse,

    @SerializedName("ancianoABVCs")
    val ancianoABVCs: List<ABVCResponse>?

):Serializable{
}