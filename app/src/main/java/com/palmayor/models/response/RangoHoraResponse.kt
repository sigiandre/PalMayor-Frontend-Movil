package com.palmayor.models.response

import com.google.gson.annotations.SerializedName

data class RangoHoraResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("inicio")
    val inicio: String,

    @SerializedName("fin")
    val fin: String
){
}