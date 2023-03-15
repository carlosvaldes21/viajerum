package com.carlosvaldes.viajerum.network

import com.carlosvaldes.viajerum.models.AuthModel
import com.carlosvaldes.viajerum.models.GenericResponse
import retrofit2.Call
import retrofit2.http.*

interface ViajerumApi {
    //Declaramos POST para el registro

    @FormUrlEncoded
    @POST("register")
    @Headers("Accept: application/json")
    fun register(

        @Field("name") name: String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<GenericResponse>

    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    fun login(
        @Field("email") email: String,
        @Field("password") password : String
    ) : Call<GenericResponse>


    @GET("places")
    @Headers("Accept: application/json")
    fun places(
        @Header("Authorization") token : String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude : String
    ) : Call<GenericResponse>

    @GET("logout")
    @Headers("Accept: application/json")
    fun logout(
        @Header("Authorization") token : String

    ) : Call<GenericResponse>

    @FormUrlEncoded
    @POST("rate")
    @Headers("Accept: application/json")
    fun rate(
        @Header("Authorization") token : String,
        @Field("comment") comment: String,
        @Field("rating") rating : String,
        @Field("place_id") placeId : String,
    ) : Call<GenericResponse>
}