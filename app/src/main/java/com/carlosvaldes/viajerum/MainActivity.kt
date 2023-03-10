package com.carlosvaldes.viajerum

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosvaldes.viajerum.databinding.ActivityMainBinding
import com.carlosvaldes.viajerum.helpers.AuthHelpers
import com.carlosvaldes.viajerum.models.GenericResponse
import com.carlosvaldes.viajerum.models.Place
import com.carlosvaldes.viajerum.network.RetrofitService
import com.carlosvaldes.viajerum.network.ViajerumApi
import com.carlosvaldes.viajerum.views.adapters.PlaceAdapter
import com.carlosvaldes.viajerum.views.adapters.PlaceHorizontalAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AuthHelpers.logout(this@MainActivity)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.rvPlaces.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvPlacesHorizontal.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        var arrayPlaces = ArrayList<Place>()
        val arrayPlacesHorizontal = ArrayList<Place>()

        arrayPlacesHorizontal.add(Place(1, "Cancún", "Un lugar muy divertido para compartie n familia", "https://images.unsplash.com/photo-1570737543098-0983d88f796d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1286&q=80",  "300", "-99.18", "18.88"))
        arrayPlacesHorizontal.add(Place(1, "Teotihuacán", "Grecia es el lugar más bonito para disfrutar con tu pareja.", "https://images.unsplash.com/photo-1586933613001-b003c20beac0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1324&q=80",  "300", "-99.18", "18.88"))
        var token = AuthHelpers.getToken(this@MainActivity)
        CoroutineScope(Dispatchers.IO).launch {

            //Llamamos al servicio de retrofit
            val call = RetrofitService.getRetrofit().create(ViajerumApi::class.java)
                .places("Bearer ${token}")

            call.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    arrayPlaces = response.body()!!.data.places!!

                    binding.rvPlaces.adapter = PlaceAdapter(this@MainActivity, arrayPlaces)
                    binding.rvPlacesHorizontal.adapter = PlaceHorizontalAdapter(this@MainActivity, arrayPlacesHorizontal)
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Verifica tu conexión a internet.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
        }




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()
        if (id == R.id.logout) {
            AuthHelpers.logout(this@MainActivity)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}