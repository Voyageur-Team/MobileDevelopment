package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.DataItem
import com.voyageur.application.databinding.ItemPlansBinding

class TripAdapter : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    private var trips = listOf<DataItem>()

    inner class TripViewHolder(private val binding: ItemPlansBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: DataItem) {
            binding.tvTitle.text = trip.title
            binding.tvDescription.text = trip.description
            binding.tvDate.text = trip.createdAt
            binding.tvParticipants.text = "${trip.participants.size} Sudah Bergabung"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemPlansBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        println("Binding trip: ${trips[position]} at position $position")
        holder.bind(trips[position])
    }


    override fun getItemCount() = trips.size

    fun submitList(newTrips: List<DataItem>) {
        trips = newTrips
        notifyDataSetChanged()
    }
}

