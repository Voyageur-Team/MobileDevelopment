package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.Iteneraries
import com.voyageur.application.databinding.ItemRecommendationBinding

class VotingAdapter(private var itineraryList: List<Iteneraries>, private val onItemClick: (Iteneraries) -> Unit) : RecyclerView.Adapter<VotingAdapter.VotingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotingViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VotingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VotingViewHolder, position: Int) {
        val itinerary = itineraryList[position]
        holder.bind(itinerary)
        holder.itemView.setOnClickListener {
            onItemClick(itinerary)
        }
    }

    override fun getItemCount(): Int = itineraryList.size

    fun updateData(newItineraries: List<Iteneraries>) {
        itineraryList = newItineraries
        notifyDataSetChanged()
    }

    class VotingViewHolder(val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itinerary: Iteneraries) {
            binding.textView.text = itinerary.itineraryName
        }
    }
}

