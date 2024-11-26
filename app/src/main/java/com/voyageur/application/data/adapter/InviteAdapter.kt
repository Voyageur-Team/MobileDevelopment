package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.Participants
import com.voyageur.application.databinding.ItemAnggotaBinding

class InviteAdapter(private var participants: List<Participants>) : RecyclerView.Adapter<InviteAdapter.ParticipantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val binding = ItemAnggotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(participants[position])
    }

    override fun getItemCount(): Int = participants.size

    fun updateParticipants(newParticipants: List<Participants>) {
        participants = newParticipants
        notifyDataSetChanged()
    }

    class ParticipantViewHolder(private val binding: ItemAnggotaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(participant: Participants) {
            binding.tvNameAnggota.text = participant.userName
        }
    }
}

