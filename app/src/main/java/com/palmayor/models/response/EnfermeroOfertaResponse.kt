package com.palmayor.models.response

import com.google.gson.annotations.SerializedName

data class EnfermeroOfertaResponse(
    @SerializedName("enfermeroId")
    val enfermeroId: Int,

    @SerializedName("ofertaId")
    val ofertaId: Int,

    @SerializedName("oferta")
    val oferta: OfertaResponse,

    @SerializedName("enfermero")
    val enfermero: EnfermeroResponse
) {

}