package com.palmayor.models.request

import com.palmayor.models.entities.FechaAtencion

data class OfertaRequest(
    val direccion: String,
    val descripcion: String?,
    val ancianoId: Int,
    val fechaAtenciones: List<FechaAtencion>
) {
}