package com.carlosvaldes.viajerum.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.carlosvaldes.viajerum.databinding.ActivityMainBinding
import com.carlosvaldes.viajerum.databinding.PlaceItemBinding
import com.carlosvaldes.viajerum.models.Place

class PlaceAdapter(private val context: Context, private var items : ArrayList<Place> ) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    class PlaceViewHolder( val binding: PlaceItemBinding ) : RecyclerView.ViewHolder( binding.root ) {
        val ivPlace = binding.ivPlace
        val tvPlaceName = binding.tvPlaceName
        val tvPlaceDescription = binding.tvPlaceDescription
        val tvPlaceCost = binding.tvPlaceCost
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = PlaceItemBinding.inflate( LayoutInflater.from(context), parent, false )
        return PlaceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.e("ITEMS", "${items.size}")
        return items.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        Glide.with(context)
            .load(items[position].img)
            .into(holder.ivPlace)

        holder.tvPlaceName.text = items[position].name
        holder.tvPlaceDescription.text = items[position].description
        holder.tvPlaceCost.text = "Promedio: $${items[position].cost}"
    }
}