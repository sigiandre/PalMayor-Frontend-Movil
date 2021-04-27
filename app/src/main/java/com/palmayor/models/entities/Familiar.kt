package com.palmayor.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "familiares")
class Familiar(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("usuarioId")
    val usuarioId: Int,

    @SerializedName("personaId")
    val personaId: Int
){
}