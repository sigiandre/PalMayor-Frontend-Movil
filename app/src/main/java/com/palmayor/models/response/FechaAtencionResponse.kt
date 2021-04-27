package com.palmayor.models.response

import com.google.gson.annotations.SerializedName

data class FechaAtencionResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("rangoHoraId")
    val rangoHoraId: Int,

    @SerializedName("ofertaId")
    val ofertaId: Int,

    @SerializedName("rangoHora")
    val rangoHora: RangoHoraResponse
){
}