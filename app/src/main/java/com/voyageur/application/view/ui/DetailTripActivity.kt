package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityDetailTripBinding

class DetailTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnRekomendasi.setOnClickListener {
            startActivity(Intent(this, VotingActivity::class.java))
        }

        binding.makeTrip.setOnClickListener {
            startActivity(Intent(this, MakeTripActivity::class.java))
        }
    }
}