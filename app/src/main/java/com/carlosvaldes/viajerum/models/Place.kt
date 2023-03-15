package com.carlosvaldes.viajerum.models

import android.media.Rating
import com.google.gson.annotations.SerializedName


data class Place(
    @SerializedName("id"          ) var id          : Int,
    @SerializedName("user_id"     ) var userId      : Int,
    @SerializedName("name"        ) var name        : String,
    @SerializedName("description" ) var description : String,
    @SerializedName("img"         ) var img         : String,
    @SerializedName("cost"        ) var cost        : String,
    @SerializedName("latitude"    ) var latitude    : String,
    @SerializedName("longitude"   ) var longitude   : String,
    @SerializedName("created_at"  ) var createdAt   : String?            = null,
    @SerializedName("updated_at"  ) var updatedAt   : String?            = null,
    @SerializedName("reviews"     ) var reviews     : ArrayList<Review> = arrayListOf(),
    @SerializedName("rating"      ) var rating      : Float
) : java.io.Serializable
