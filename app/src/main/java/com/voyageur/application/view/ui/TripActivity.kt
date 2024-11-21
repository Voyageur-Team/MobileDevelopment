package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityTripBinding

class TripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTripBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnCreate.setOnClickListener {
            startActivity(Intent(this, InviteActivity::class.java))
        }

    }
}