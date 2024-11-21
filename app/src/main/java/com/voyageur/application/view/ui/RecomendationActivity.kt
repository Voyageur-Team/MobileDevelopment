package com.voyageur.application.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityRecomendationBinding

class RecomendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecomendationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomendationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}