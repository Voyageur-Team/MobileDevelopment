package com.voyageur.application.view.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.voyageur.application.R
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
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = "Buat Rencana Bersama"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AppPreferences.getInstance(applicationContext.dataStore)

        observeViewModel()
        fetchCities()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        makeTripViewModel.cities.observe(this) { cities ->
            val cityNames = cities.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cityNames)
            binding.etTujuan1.setAdapter(adapter)
            binding.etTujuan2.setAdapter(adapter)
        }

        makeTripViewModel.isError.observe(this) { isError ->
            if (isError) {
                val message = makeTripViewModel.message.value
                if (message != null) {
                    println("Error: $message")
                } else {
                    println("Error: Unknown error occurred")
                }
            }
        }

        makeTripViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}
