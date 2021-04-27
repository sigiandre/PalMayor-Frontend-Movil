package com.palmayor.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "roles")
data class Rol(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String
){
}