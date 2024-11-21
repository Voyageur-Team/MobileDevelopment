package com.voyageur.application.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityCityBinding

class CityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}