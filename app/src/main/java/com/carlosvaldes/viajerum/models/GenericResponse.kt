package com.carlosvaldes.viajerum.models

import com.google.gson.annotations.SerializedName

data class GenericResponse(
    val code : Int,
    val message: String,
    val data: Data,
    val token: String?
)


data class Data(
    val places: ArrayList<Place>?,
    @SerializedName("nearby_places")
    val nearbyPlaces: ArrayList<Place>?,
    val user: User?
)
