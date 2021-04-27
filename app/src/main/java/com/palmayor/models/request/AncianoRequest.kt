package com.palmayor.models.request

data class AncianoRequest(
    val familiarId : Int,
    val persona: PersonaRequest,
    val ancianoABVCs : List<AncianoABVCRequest>
) {
}