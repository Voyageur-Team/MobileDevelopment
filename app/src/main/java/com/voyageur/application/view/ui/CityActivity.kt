package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.data.adapter.CitiesAdapter
import com.voyageur.application.databinding.ActivityCityBinding
import com.voyageur.application.viewmodel.PreferencesViewModel

class CityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityBinding
    private lateinit var citiesViewModel: PreferencesViewModel

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

        citiesViewModel.cities
    }

    private fun setupRecyclerView() {
        binding.rvCities.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        citiesViewModel.cities.observe(this) { citiesList ->
            if (citiesList.isNotEmpty()) {
                val adapter = CitiesAdapter(citiesList)
                binding.rvCities.adapter = adapter
            }
        }
    }
}
