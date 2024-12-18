package com.voyageur.application.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.data.model.DataUserEmail
import com.voyageur.application.data.model.Participants
import com.voyageur.application.databinding.ItemAnggotaBinding
import com.voyageur.application.databinding.ItemUserSearchBinding

class InviteAdapterParticipants(
    private var participants: List<Participants>,
    private val onDelete: (String, String) -> Unit
) : RecyclerView.Adapter<InviteAdapterParticipants.ParticipantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val binding = ItemAnggotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantViewHolder(binding, onDelete)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(participants[position], position + 1)
    }

    override fun getItemCount(): Int = participants.size

    fun updateParticipants(newParticipants: List<Participants>) {
        participants = newParticipants
        notifyDataSetChanged()
    }

    class ParticipantViewHolder(
        private val binding: ItemAnggotaBinding,
        private val onDelete: (String, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(participant: Participants, position: Int) {
            binding.tvNumber.text = position.toString()
            binding.tvNameAnggota.text = participant.userName
            binding.tvEmailAnggota.text = participant.email

            binding.btnDelete.setOnClickListener {
                val userId = participant.userId ?: ""
                val userName = participant.userName ?: ""
                if (userId.isNotEmpty() && userName.isNotEmpty()) {
                    onDelete(userId, userName)
                } else {
                    Toast.makeText(itemView.context, "Data incomplete", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}



class InviteAdapterUsers(private var users: List<DataUserEmail>, private val onAddClick: (DataUserEmail) -> Unit) : RecyclerView.Adapter<InviteAdapterUsers.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = ItemUserSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding, onAddClick)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<DataUserEmail>) {
        users = newUsers
        notifyDataSetChanged()
    }

    class UsersViewHolder(private val binding: ItemUserSearchBinding, private val onAddClick: (DataUserEmail) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: DataUserEmail) {
            binding.tvName.text = user.userName
            binding.tvEmail.text = user.email

            binding.ivAdd.setOnClickListener {
                if (user.userId.isNullOrEmpty()) {
                    Toast.makeText(itemView.context, "User ID is missing", Toast.LENGTH_SHORT).show()
                } else {
                    onAddClick(user)
                }
            }
        }
    }
}


