package com.carlosvaldes.viajerum.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosvaldes.viajerum.MainActivity
import com.carlosvaldes.viajerum.databinding.PlaceItemBinding
import com.carlosvaldes.viajerum.databinding.ReviewItemBinding
import com.carlosvaldes.viajerum.models.Place
import com.carlosvaldes.viajerum.models.Review

class ReviewAdapter(private val context: Context, private var items : ArrayList<Review> ) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    class ReviewViewHolder( val binding: ReviewItemBinding) : RecyclerView.ViewHolder( binding.root ) {
        val tvName = binding.tvName
        val tvComment = binding.tvComment
        val tvRating = binding.tvRating
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ReviewItemBinding.inflate( LayoutInflater.from(context), parent, false )
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.tvName.text = items[position].name
        holder.tvComment.text = items[position].comment
        holder.tvRating.text = items[position].rating.toString()

    }
}