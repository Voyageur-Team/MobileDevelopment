package com.voyageur.application.data.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.IteneraryItem
import com.voyageur.application.databinding.ItemItinerariesBinding
import com.voyageur.application.view.utils.Formatted

class ItineraryAdapter(private var itineraryList: List<IteneraryItem>) : RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryViewHolder {
        val binding = ItemItinerariesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItineraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItineraryViewHolder, position: Int) {
        holder.bind(itineraryList[position])
    }

    override fun getItemCount(): Int = itineraryList.size

    fun updateData(newItineraries: List<IteneraryItem>) {
        itineraryList = newItineraries
        notifyDataSetChanged()
    }

    class ItineraryViewHolder(private val binding: ItemItinerariesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itineraryItem: IteneraryItem) {
            binding.tvTitle.text = itineraryItem.name
            binding.tvBudget.text = Formatted.formatRupiah(itineraryItem.price.toDouble())
            binding.tvLocation.text = "Lihat di Google Maps"
            binding.tvLocation.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(itineraryItem.location))
                it.context.startActivity(intent)
            }
        }
    }
}