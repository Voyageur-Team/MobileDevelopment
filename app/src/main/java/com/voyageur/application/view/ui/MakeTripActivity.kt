package com.voyageur.application.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityMakeTripBinding

class MakeTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMakeTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

    }
}