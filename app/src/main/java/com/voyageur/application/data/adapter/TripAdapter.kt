package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.DataItem
import com.voyageur.application.databinding.ItemPlansBinding

class TripAdapter(private val onItemClick: (DataItem) -> Unit) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    private var trips = listOf<DataItem>()

    inner class TripViewHolder(private val binding: ItemPlansBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: DataItem) {
            binding.tvTitle.text = trip.title
            binding.tvDescription.text = trip.description
            binding.tvStartDate.text = if (trip.tripStartDate.isNullOrEmpty()) "Belum ditentukan" else trip.tripStartDate
            binding.tvEndDate.text = trip.tripEndDate
            binding.tvParticipants.text = "${trip.participants.size} Sudah Bergabung"

            binding.root.setOnClickListener {
                onItemClick(trip)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemPlansBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    override fun getItemCount() = trips.size

    fun submitList(newTrips: List<DataItem>) {
        trips = newTrips
        notifyDataSetChanged()
    }
}