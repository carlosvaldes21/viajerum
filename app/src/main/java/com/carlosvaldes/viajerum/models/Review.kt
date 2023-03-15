package com.carlosvaldes.viajerum.models

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id"         ) var id        : Int,
    @SerializedName("user_id"    ) var userId    : Int,
    @SerializedName("place_id"   ) var placeId   : Int,
    @SerializedName("rating"     ) var rating    : Float,
    @SerializedName("comment"    ) var comment   : String,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("name"       ) var name      : String? = null
) : java.io.Serializable
