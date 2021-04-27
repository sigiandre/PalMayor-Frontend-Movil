package com.palmayor.models.request

data class EnfermeroRequest(
    val colegiatura: String,
    val universidad: String,
    val experiencia: String?,
    val usuarioId: Int,
    val especialidadId: Int,
    val gradoId: Int,
    val persona: PersonaRequest
){}