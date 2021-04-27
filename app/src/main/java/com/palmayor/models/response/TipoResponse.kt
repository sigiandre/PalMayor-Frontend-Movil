package com.palmayor.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TipoResponse(
    @SerializedName("id")
    val id : Int,

    @SerializedName("nombre")
    val nombre : String
):Serializable{
}