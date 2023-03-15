package com.carlosvaldes.viajerum.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.carlosvaldes.viajerum.databinding.ActivityDetailBinding
import com.carlosvaldes.viajerum.models.Place
import com.carlosvaldes.viajerum.views.adapters.ReviewAdapter


class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        val place = bundle?.getSerializable("place") as Place

        Glide.with(this@DetailActivity)
            .load(place.img)
            .into(binding.ctLayout.ivPlace)

        binding.tvName.text = place.name
        binding.tvDescription.text = place.description
        binding.tvRating.text = place.rating.toString()
        binding.rbPlaces.rating = place.rating

        binding.rvReviews.addItemDecoration(
            DividerItemDecoration(
                this@DetailActivity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvReviews.layoutManager = LinearLayoutManager(this@DetailActivity)
        binding.rvReviews.adapter = ReviewAdapter(this@DetailActivity, place.reviews)
        if ( place.wasReviewed ) {
            binding.buttonRating.text = "Editar reseña"
        } else {
            binding.buttonRating.text = "Agregar reseña"
        }
        binding.buttonDirections.setOnClickListener {
            val strUri =
                "http://maps.google.com/maps?q=loc:${place.latitude},${place.longitude} (${place.name})"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            startActivity(intent)
        }

        binding.buttonRating.setOnClickListener {
            val params = Bundle().apply {
                putSerializable("place", place)
            }

            val intent = Intent(this, AddReviewActivity::class.java).apply {
                putExtras(params)
            }

            startActivity(intent)
        }

        binding.ctLayout.toolbar.setTitle(place.name)
        setSupportActionBar(binding.ctLayout.toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}