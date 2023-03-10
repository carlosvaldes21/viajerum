package com.carlosvaldes.viajerum.network

import com.carlosvaldes.viajerum.models.GenericResponse
import retrofit2.Call
import retrofit2.http.*

interface ViajerumApi {
    //Declaramos POST para el registro
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<GenericResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password : String
    ) : Call<GenericResponse>

    @GET("places")
    fun places(
        @Header("Authorization") token : String
    ) : Call<GenericResponse>

    @GET("logout")
    fun logout(
        @Header("Authorization") token : String
    ) : Call<GenericResponse>
}