package com.palmayor.models.response

import com.google.gson.annotations.SerializedName

data class OfertaResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("ancianoId")
    val anciandoId: Int,

    @SerializedName("anciano")
    val anciano: AncianoResponse,

    @SerializedName("fechaAtenciones")
    val fechaAtenciones: List<FechaAtencionResponse>
){
}