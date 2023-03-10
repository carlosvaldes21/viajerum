package com.carlosvaldes.viajerum.models

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("user_id")
    val userId: Int,
    val name: String,
    val description: String,
    val img: String,
    val cost: String,
    val latitude: String,
    val longitude: String
)
