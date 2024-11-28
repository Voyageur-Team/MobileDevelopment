package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityDetailPopularBinding

class DetailPopularActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPopularBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPopularBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val name = intent.getStringExtra("name")
        val image = intent.getIntExtra("image", -1)
        val description = intent.getStringExtra("description")
        val location = intent.getStringExtra("location")
        val rating = intent.getFloatExtra("rating", 0f)
        val price = intent.getFloatExtra("price", 0f)

        binding.apply {
            imgPopularDestination.setImageResource(image)
            namePopularDestination.text = name
            descriptionPopularDestination.text = description
            locationPopularDestination.text = location
            ratingPopularDestination.text = rating.toString()
            pricePopularDestination.text = price.toString()
        }

        binding.btnRencana.setOnClickListener {
            startActivity(Intent(this, TripActivity::class.java))
        }
    }
}