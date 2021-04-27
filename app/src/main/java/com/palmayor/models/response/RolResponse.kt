package com.palmayor.models.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class RolResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String
){
}