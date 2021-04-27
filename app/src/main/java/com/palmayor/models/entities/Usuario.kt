package com.palmayor.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("correo")
    val correo: String,

    @SerializedName("rolId")
    val rolId: Int
){
}