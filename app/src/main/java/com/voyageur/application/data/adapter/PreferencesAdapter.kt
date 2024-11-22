package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.Preferences
import com.voyageur.application.databinding.ItemPreferencesBinding

class PreferencesAdapter(private val preferencesList: List<Preferences>) :
    RecyclerView.Adapter<PreferencesAdapter.PreferenceViewHolder>() {

    inner class PreferenceViewHolder(val binding: ItemPreferencesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferenceViewHolder {
        val binding = ItemPreferencesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreferenceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder, position: Int) {
        val preference = preferencesList[position]
        holder.binding.tvPreference.text = preference.name
    }

    override fun getItemCount(): Int = preferencesList.size
}