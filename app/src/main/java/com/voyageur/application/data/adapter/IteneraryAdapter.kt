package com.voyageur.application.data.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.IteneraryItem
import com.voyageur.application.databinding.ItemItinerariesBinding
import com.voyageur.application.view.utils.Formatted

class IteneraryAdapter(private var itineraryList: List<IteneraryItem>) : RecyclerView.Adapter<IteneraryAdapter.IteneraryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IteneraryViewHolder {
        val binding = ItemItinerariesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IteneraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IteneraryViewHolder, position: Int) {
        holder.bind(itineraryList[position])
    }

    override fun getItemCount(): Int = itineraryList.size

    fun updateData(newItineraries: List<IteneraryItem>) {
        itineraryList = newItineraries
        notifyDataSetChanged()
    }

    class IteneraryViewHolder(private val binding: ItemItinerariesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(iteneraryItem: IteneraryItem) {
            binding.tvTitle.text = iteneraryItem.name
            binding.tvBudget.text = Formatted.formatRupiah(iteneraryItem.price.toDouble())
            binding.tvRating.text = Formatted.formatRating(iteneraryItem.rating.toDouble())
            binding.tvCategory.text = iteneraryItem.category
            binding.tvLocation.text = "Lihat di Google Maps"
            binding.tvLocation.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(iteneraryItem.location))
                it.context.startActivity(intent)
            }
        }
    }
}