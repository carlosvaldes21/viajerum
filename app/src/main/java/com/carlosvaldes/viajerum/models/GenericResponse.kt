package com.carlosvaldes.viajerum.models

data class GenericResponse(
    val message: String,
    val data: Data,
    val token: String?
)

class Data(
    val places: ArrayList<Place>?,
    val user: User?
)
