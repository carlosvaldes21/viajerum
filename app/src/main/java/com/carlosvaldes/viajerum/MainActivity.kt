package com.carlosvaldes.viajerum

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosvaldes.viajerum.databinding.ActivityMainBinding
import com.carlosvaldes.viajerum.helpers.AuthHelpers
import com.carlosvaldes.viajerum.helpers.Helpers.Companion.isOnline
import com.carlosvaldes.viajerum.models.GenericResponse
import com.carlosvaldes.viajerum.models.Place
import com.carlosvaldes.viajerum.network.RetrofitService
import com.carlosvaldes.viajerum.network.ViajerumApi
import com.carlosvaldes.viajerum.views.DetailActivity
import com.carlosvaldes.viajerum.views.adapters.PlaceAdapter
import com.carlosvaldes.viajerum.views.adapters.PlaceHorizontalAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), LocationListener {
    lateinit var binding : ActivityMainBinding

    //Para los permisos
    private var coarseLocationPermissionGranted = false
    private var fineLocationPermissionGranted = false
    
    private var makeRequest = true

    companion object{
        const val PERMISSION_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AuthHelpers.logout(this@MainActivity)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        updateOrRequestPermissions()
        binding.pbLoader.visibility = View.VISIBLE
        binding.rvPlaces.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvPlacesHorizontal.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)


    }

    private fun getPlaces(latitude: String, longitude : String) {
        if ( isOnline(this@MainActivity) ) {
            var arrayPlaces = ArrayList<Place>()
            var arrayPlacesHorizontal = ArrayList<Place>()

            //arrayPlacesHorizontal.add(Place(1, "Cancún", "Un lugar muy divertido para compartie n familia", "https://images.unsplash.com/photo-1570737543098-0983d88f796d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1286&q=80",  "300", "-99.18", "18.88"))
            //arrayPlacesHorizontal.add(Place(1, "Teotihuacán", "Grecia es el lugar más bonito para disfrutar con tu pareja.", "https://images.unsplash.com/photo-1586933613001-b003c20beac0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1324&q=80",  "300", "-99.18", "18.88"))
            var token = AuthHelpers.getToken(this@MainActivity)
            CoroutineScope(Dispatchers.IO).launch {

                //Llamamos al servicio de retrofit
                val call = RetrofitService.getRetrofit().create(ViajerumApi::class.java)
                    .places("Bearer ${token}", latitude, longitude)

                call.enqueue(object : Callback<GenericResponse> {
                    override fun onResponse(
                        call: Call<GenericResponse>,
                        response: Response<GenericResponse>
                    ) {

                        if ( response.body() == null ) {
                            AuthHelpers.logout(this@MainActivity)
                            return
                        }

                        Log.d("RESPUESTA", "Respuesta del servidor: ${response.toString()}")
                        Log.d("RESPUESTA", "Datos: ${response.body()!!.data.places.toString()}")
                        arrayPlaces = response.body()!!.data!!.places!!
                        arrayPlacesHorizontal = response.body()!!.data!!.nearbyPlaces!!

                        binding.rvPlaces.adapter = PlaceAdapter(this@MainActivity, arrayPlaces)
                        binding.rvPlacesHorizontal.adapter = PlaceHorizontalAdapter(this@MainActivity, arrayPlacesHorizontal)
                        binding.pbLoader.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                        binding.pbLoader.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            "No podemos procesar tu petición en este momento.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
            }
        } else {
            Toast.makeText(this@MainActivity, "Para usar la aplicación, debes conectarte a internet", Toast.LENGTH_LONG).show()
            binding.pbLoader.visibility = View.GONE
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

    private fun updateOrRequestPermissions() {

        //Revisando los permisos
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        coarseLocationPermissionGranted = hasCoarseLocationPermission
        fineLocationPermissionGranted = hasFineLocationPermission

        //Solicitando los permisos

        val permissionsToRequest = mutableListOf<String>()

        if (!hasCoarseLocationPermission)
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (!hasFineLocationPermission)
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)


        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_LOCATION
            )
        } else {

            //Tenemos los permisos
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000,
                10F,
                this
            )

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Se obtuvo el permiso
                    updateOrRequestPermissions()
                } else {
                    if (shouldShowRequestPermissionRationale(permissions[0])) {
                        AlertDialog.Builder(this)
                            .setTitle("Permiso requerido")
                            .setMessage("Se necesita acceder a la ubicación para esta mostrarle los lugares cercanos")
                            .setPositiveButton(
                                "Entendido",
                                DialogInterface.OnClickListener { _, _ ->
                                    updateOrRequestPermissions()
                                })
                            .setNegativeButton(
                                "Salir",
                                DialogInterface.OnClickListener { dialog, _ ->
                                    dialog.dismiss()
                                    finish()
                                })
                            .create()
                            .show()
                    } else {
                        //Si el usuario no quiere que nunca se le vuelva a preguntar por el permiso
                        Toast.makeText(
                            this,
                            "El permiso a la localización se ha negado permanentemente",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        //TODO Implement swipe refres
        if ( makeRequest ) {
            getPlaces(location.latitude.toString(), location.longitude.toString())
            makeRequest = false
        }

    }

    override fun onRestart() {
        super.onRestart()
        if(!fineLocationPermissionGranted){
            updateOrRequestPermissions()
        }
    }

    fun onTapPlace(place : Place) {
        val params = Bundle().apply {
            putSerializable("place", place)
        }

        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtras(params)
        }

        startActivity(intent)
    }
}