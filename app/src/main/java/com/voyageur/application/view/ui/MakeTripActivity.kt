package com.voyageur.application.view.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityMakeTripBinding
import com.voyageur.application.viewmodel.MakeTripViewModel
import kotlinx.coroutines.launch

class MakeTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMakeTripBinding
    private val makeTripViewModel: MakeTripViewModel by viewModels()
    private lateinit var pref: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        pref = AppPreferences.getInstance(applicationContext.dataStore)

        observeViewModel()
        fetchCities()
    }

    private fun fetchCities() {
        lifecycleScope.launch {
            pref.getToken().collect { token ->
                if (token.isNotEmpty()) {
                    makeTripViewModel.getAllCities(token)
                }
            }
        }
    }

    private fun observeViewModel() {
        // Observe cities list and set it to the AutoCompleteTextViews
        makeTripViewModel.cities.observe(this) { cities ->
            val cityNames = cities.map { it.name } // Extract city names
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cityNames)
            binding.etTujuan1.setAdapter(adapter)
            binding.etTujuan2.setAdapter(adapter)
        }

        // Observe error state
        makeTripViewModel.isError.observe(this) { isError ->
            if (isError) {
                val message = makeTripViewModel.message.value
                if (message != null) {
                    println("Error: $message") // Log the error message
                } else {
                    println("Error: Unknown error occurred") // Log a default error message
                }
            }
        }

        // Observe loading state
        makeTripViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}
