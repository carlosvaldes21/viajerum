package com.carlosvaldes.viajerum.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.carlosvaldes.viajerum.MainActivity
import com.carlosvaldes.viajerum.databinding.ActivityAddReviewBinding
import com.carlosvaldes.viajerum.helpers.AuthHelpers
import com.carlosvaldes.viajerum.helpers.Helpers.Companion.setupUI
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


class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding

    private lateinit var place : Place
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        place = bundle?.getSerializable("place") as Place

        binding.buttonAddReview.setOnClickListener {
            onClickAdd()
        }

        setupUI(binding.root, this@AddReviewActivity)
    }

    fun onClickAdd()
    {
        binding.tvError.text = ""
        //binding.pbLoader.visibility = View.VISIBLE

        var comment = binding.etComment.text.toString().trim()
        var rating = binding.rbReview.rating.toDouble()
        var userId = place.userId
        var placeId = place.id
        if ( !validateFields(comment, rating) ) {
            //binding.pbLoader.visibility = View.GONE
            return
        }

        review(comment, rating.toString(), placeId.toString())
    }

    fun validateFields(comment: String, rating: Double) : Boolean {
        if ( comment.length <= 0 ) {
            binding.tvError.text = "Debes añadir un comentario"
            return false
        }
        if ( rating == 0.0 ) {
            binding.tvError.text = "Debes proporcionar una calificación"
            return false
        }

        return true
    }

    private fun review(comment: String, rating: String, placeId: String) {

        binding.pbLoader.visibility = View.VISIBLE
        binding.buttonAddReview.visibility = View.INVISIBLE
        var token = AuthHelpers.getToken(this@AddReviewActivity)
        CoroutineScope(Dispatchers.IO).launch {

            //Llamamos al servicio de retrofit
            val call = RetrofitService.getRetrofit().create(ViajerumApi::class.java)
                .rate("Bearer ${token}", comment, rating, placeId)

            call.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {

                    if ( response.body() == null ) {
                        Toast.makeText(this@AddReviewActivity, "Tu sesión ha expirado, inténtalo de nuevo", Toast.LENGTH_LONG).show()

                        //AuthHelpers.logout(this@AddReviewActivity)
                        return
                    }

                    if ( response.body()!!.code == 200 ) {
                        Toast.makeText(this@AddReviewActivity, "Tu opinión fue agregada correctamente", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@AddReviewActivity, MainActivity::class.java)
                        this@AddReviewActivity.startActivity(intent)
                        this@AddReviewActivity.finish()
                        return
                    }

                    binding.tvError.text = response.body()!!.message
                    binding.pbLoader.visibility = View.GONE
                    binding.buttonAddReview.visibility = View.VISIBLE
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    binding.pbLoader.visibility = View.GONE
                    binding.buttonAddReview.visibility = View.VISIBLE
                    Toast.makeText(
                        this@AddReviewActivity,
                        "Verifica tu conexión a internet. ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
        }
    }

}