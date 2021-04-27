package com.palmayor.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ABVCResponse(
    @SerializedName("id")
    val id : Int,

    @SerializedName("descripcion")
    val descripcion : String,

    @SerializedName("tipoId")
    val tipoId : Int,

    @SerializedName("tipo")
    val tipo : TipoResponse
) :Serializable{
}