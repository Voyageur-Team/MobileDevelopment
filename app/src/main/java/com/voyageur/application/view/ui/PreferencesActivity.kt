package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.voyageur.application.data.adapter.PreferencesAdapter
import com.voyageur.application.databinding.ActivityPreferencesBinding
import com.voyageur.application.viewmodel.PreferencesViewModel
import com.voyageur.application.viewmodel.PreferencesViewModelFactory

class PreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferencesBinding
    private val preferencesViewModel: PreferencesViewModel by viewModels {
        PreferencesViewModelFactory(FirebaseFirestore.getInstance())
    }

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

        preferencesViewModel.fetchPreferences()
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
    }
}
