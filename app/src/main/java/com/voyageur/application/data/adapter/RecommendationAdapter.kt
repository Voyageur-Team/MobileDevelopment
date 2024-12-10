package com.voyageur.application.data.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.DividedItenerary
import com.voyageur.application.data.model.DividedIteneraryItems
import com.voyageur.application.databinding.ItemRecommendationsDayBinding
import com.voyageur.application.databinding.ItemRecommendationsIteneraryBinding
import com.voyageur.application.view.utils.Formatted

class RecommendationAdapterIteneraries(private var dividedList: List<DividedIteneraryItems>) : RecyclerView.Adapter<RecommendationAdapterIteneraries.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationsIteneraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(dividedList[position])
    }

    override fun getItemCount(): Int = dividedList.size

    fun updateData(newItems: List<DividedIteneraryItems>) {
        dividedList = newItems
        notifyDataSetChanged()
    }

    class RecommendationViewHolder(private val binding: ItemRecommendationsIteneraryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dividedItenerary: DividedIteneraryItems) {
            binding.tvTitle.text = dividedItenerary.name
            binding.tvBudget.text = Formatted.formatRupiah(dividedItenerary.price.toDouble())
            binding.tvRating.text = Formatted.formatRating(dividedItenerary.rating.toDouble())
            binding.tvCategory.text = dividedItenerary.category
            binding.tvLocation.text = "Lihat di Google Maps"
            binding.tvLocation.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dividedItenerary.location))
                it.context.startActivity(intent)
            }
        }
    }
}


class RecommendationAdapterDays(
    private var dividedList: List<DividedItenerary>,
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<RecommendationAdapterDays.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationsDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding, onDayClick)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(dividedList[position])
    }

    override fun getItemCount(): Int = dividedList.size

    fun updateData(newItems: List<DividedItenerary>) {
        dividedList = newItems
        notifyDataSetChanged()
    }

    class RecommendationViewHolder(
        private val binding: ItemRecommendationsDayBinding,
        private val onDayClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dividedItenerary: DividedItenerary) {
            binding.tvDay.text = "Day ${dividedItenerary.day}"
            binding.root.setOnClickListener {
                onDayClick(dividedItenerary.day)
            }
        }
    }
}