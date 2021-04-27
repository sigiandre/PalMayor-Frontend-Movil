package com.palmayor.models.request

import com.google.gson.annotations.SerializedName

data class AncianoABVCRequest (
    @SerializedName("abvcId")
    val abvcId: Int
){
}