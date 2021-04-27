package com.palmayor.models.response

import com.google.gson.annotations.SerializedName

data class GradoResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String
){
}