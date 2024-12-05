package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.Preferences
import com.voyageur.application.databinding.ItemPreferencesBinding

class MakeTripAdapter(
    private val preferencesList: List<Preferences>,
    private val onItemClick: (Preferences) -> Unit
) : RecyclerView.Adapter<MakeTripAdapter.MakeTripViewHolder>() {

    inner class MakeTripViewHolder(val binding: ItemPreferencesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(preferencesList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeTripViewHolder {
        val binding = ItemPreferencesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MakeTripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MakeTripViewHolder, position: Int) {
        val preference = preferencesList[position]
        holder.binding.tvPreference.text = preference.name
    }

    override fun getItemCount(): Int = preferencesList.size
}
