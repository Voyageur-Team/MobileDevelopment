package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.Iteneraries
import com.voyageur.application.databinding.ItemRecommendationBinding

class VotingAdapter(
    private var items: List<Iteneraries>,
    private val onItemClick: (Iteneraries) -> Unit
) : RecyclerView.Adapter<VotingAdapter.ViewHolder>() {

    private var selectedPosition = -1

    inner class ViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Iteneraries, position: Int) {
            binding.textView.text = "Rekomendasi ${position + 1}"
            binding.tvVotes.text = "Dipilih oleh ${item.votes?.size ?: 0} peserta"
            binding.radioButton.isChecked = position == selectedPosition
            binding.radioButton.setOnClickListener {
                if (selectedPosition != position) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = position
                    notifyItemChanged(selectedPosition)
                }
            }
            binding.cardView2.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Iteneraries>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun getSelectedItinerary(): Iteneraries? {
        return if (selectedPosition != -1) items[selectedPosition] else null
    }
}