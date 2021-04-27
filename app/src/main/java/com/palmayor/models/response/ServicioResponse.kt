package com.palmayor.models.response

import com.google.gson.annotations.SerializedName

data class ServicioResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("enfermeroId")
    val enfermeroId: Int,

    @SerializedName("ofertaId")
    val ofertaId: Int,

    @SerializedName("enfermero")
    val enfermero: EnfermeroResponse,

    @SerializedName("oferta")
    val oferta: OfertaResponse
){
}