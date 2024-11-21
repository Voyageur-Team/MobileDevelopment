package com.voyageur.application.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}