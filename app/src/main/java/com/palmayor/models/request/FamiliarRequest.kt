package com.palmayor.models.request

data class FamiliarRequest(
    val usuarioId: Int,
    val persona: PersonaRequest
){
}