package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.data.adapter.PreferencesAdapter
import com.voyageur.application.databinding.ActivityPreferencesBinding
import com.voyageur.application.viewmodel.PreferencesViewModel

class PreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferencesBinding
    private lateinit var preferencesViewModel: PreferencesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupRecyclerView()
        observeViewModel()

        binding.tvNext.setOnClickListener {
            startActivity(Intent(this, CityActivity::class.java))
        }

        preferencesViewModel.getAllPreferences()
    }

    private fun setupRecyclerView() {
        binding.rvPreferences.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        preferencesViewModel.preferences.observe(this) { preferencesList ->
            if (preferencesList.isNotEmpty()) {
                val adapter = PreferencesAdapter(preferencesList)
                binding.rvPreferences.adapter = adapter
            } else {
                Log.w("PreferencesActivity", "No preferences found")
            }
        }

        preferencesViewModel.isError.observe(this) { isError ->
            if (isError) {
                Log.e("PreferencesActivity", "Error fetching preferences")
            }
        }

        preferencesViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
