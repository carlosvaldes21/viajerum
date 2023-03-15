package com.carlosvaldes.viajerum.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosvaldes.viajerum.MainActivity
import com.carlosvaldes.viajerum.databinding.PlaceHorizontalItemBinding
import com.carlosvaldes.viajerum.models.Place

class PlaceHorizontalAdapter(private val context: Context, private var items : ArrayList<Place> ) : RecyclerView.Adapter<PlaceHorizontalAdapter.PlaceViewHolder>() {
    class PlaceViewHolder( val binding: PlaceHorizontalItemBinding) : RecyclerView.ViewHolder( binding.root ) {
        val ivPlace = binding.ivPlace
        val tvPlaceName = binding.tvPlaceName

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = PlaceHorizontalItemBinding.inflate( LayoutInflater.from(context), parent, false )
        return PlaceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        Glide.with(context)
            .load(items[position].img)
            .into(holder.ivPlace)

        holder.tvPlaceName.text = items[position].name

        holder.itemView.setOnClickListener{
            if(context is MainActivity) context.onTapPlace(items[position])
        }
    }
}