package com.palmayor.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ancianos")
class Anciano(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("familiarId")
    val familiarId: Int,

    @SerializedName("personaId")
    val personaId: Int
){
}