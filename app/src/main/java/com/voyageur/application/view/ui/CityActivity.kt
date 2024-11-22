package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.voyageur.application.data.adapter.CitiesAdapter
import com.voyageur.application.databinding.ActivityCityBinding
import com.voyageur.application.viewmodel.PreferencesViewModel
import com.voyageur.application.viewmodel.PreferencesViewModelFactory

class CityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityBinding
    private val viewModel: PreferencesViewModel by viewModels {
        PreferencesViewModelFactory(FirebaseFirestore.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupRecyclerView()
        observeViewModel()

        binding.tvDone.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        viewModel.fetchCities()
    }

    private fun setupRecyclerView() {
        binding.rvCities.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.cities.observe(this) { citiesList ->
            if (citiesList.isNotEmpty()) {
                val adapter = CitiesAdapter(citiesList)
                binding.rvCities.adapter = adapter
            }
        }
    }
}
