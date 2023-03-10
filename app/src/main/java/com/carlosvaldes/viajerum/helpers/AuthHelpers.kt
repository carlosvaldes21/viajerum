package com.carlosvaldes.viajerum.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.carlosvaldes.viajerum.MainActivity
import com.carlosvaldes.viajerum.R
import com.carlosvaldes.viajerum.models.GenericResponse
import com.carlosvaldes.viajerum.network.RetrofitService
import com.carlosvaldes.viajerum.network.ViajerumApi
import com.carlosvaldes.viajerum.views.adapters.PlaceAdapter
import com.carlosvaldes.viajerum.views.adapters.PlaceHorizontalAdapter
import com.carlosvaldes.viajerum.views.auth.AuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthHelpers {

    companion object {
        fun tokenExist(activity: Activity) : Boolean {
            var token = getToken(activity)

            if ( token.length > 0 ) {
                val intent = Intent(activity, MainActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
                return true
            }

            return false
        }

        fun getToken(activity: Activity) : String {
            val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            val token = sharedPref.getString("auth_token", "").toString()

            if ( token.length > 0 ) {
                return token
            }

            return ""
        }

        fun storeToken(token: String?, activity : Activity?)
        {
            val sharedPref = activity?.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString("auth_token", token)
                apply()
            }
        }

        fun logout(activity : Activity) {
            var token = getToken(activity)

            if ( token.length > 0 ) {
                val sharedPref = activity?.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    remove("auth_token")
                    apply()
                }

                val intent = Intent(activity, AuthActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
                CoroutineScope(Dispatchers.IO).launch {

                    //Llamamos al servicio de retrofit
                    val call = RetrofitService.getRetrofit().create(ViajerumApi::class.java)
                        .logout("Bearer ${token}")

                    call.enqueue(object : Callback<GenericResponse> {
                        override fun onResponse(
                            call: Call<GenericResponse>,
                            response: Response<GenericResponse>
                        ) {


                        }

                        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                            Toast.makeText(
                                activity,
                                "No hay conexión. Error: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
                }
            }

        }
    }
}