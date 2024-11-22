package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.Cities
import com.voyageur.application.databinding.ItemCitiesBinding

class CitiesAdapter(private val citiesList: List<Cities>) :
    RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {

    inner class CityViewHolder(val binding: ItemCitiesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = ItemCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = citiesList[position]
        holder.binding.tvCity.text = city.name
    }

    override fun getItemCount(): Int = citiesList.size
}
