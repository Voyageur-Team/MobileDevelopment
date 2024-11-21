package com.voyageur.application.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.voyageur.application.R
import com.voyageur.application.data.model.PopularDestination
import com.voyageur.application.view.ui.DetailPopularActivity

class PopularDestinationAdapter(private val listHero: ArrayList<PopularDestination>) : RecyclerView.Adapter<PopularDestinationAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_destination, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, image, rating, location, description, price) = listHero[position]
        holder.imgPhoto.setImageResource(image)
        holder.tvName.text = name
        holder.tvLocation.text = location

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailPopularActivity::class.java).apply {
                putExtra("name", name)
                putExtra("image", image)
                putExtra("description", description)
                putExtra("location", location)
                putExtra("rating", rating)
                putExtra("price", price)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listHero.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imgPopularDestination)
        val tvName: TextView = itemView.findViewById(R.id.namePopularDestination)
        val tvLocation: TextView = itemView.findViewById(R.id.locationPopularDestination)
    }
}