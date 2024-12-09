package com.voyageur.application.view.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.R
import com.voyageur.application.data.adapter.IteneraryAdapter
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityIteneraryBinding
import com.voyageur.application.viewmodel.IteneraryViewModel
import kotlinx.coroutines.launch

class IteneraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIteneraryBinding
    private lateinit var adapter: IteneraryAdapter
    private lateinit var itineraryId: String
    private lateinit var tripId: String
    private lateinit var pref: AppPreferences

    private val itineraryViewModel: IteneraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIteneraryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Itinerary"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AppPreferences.getInstance(applicationContext.dataStore)
        itineraryId = intent.getStringExtra("ITINERARY_ID") ?: ""
        tripId = intent.getStringExtra("TRIP_ID") ?: ""

        if (itineraryId.isEmpty() || tripId.isEmpty()) {
            Toast.makeText(this, "Invalid itinerary or trip ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupUI()
        observeViewModel()
        fetchData()
    }

    private fun fetchData() {
        if (itineraryId.isNotEmpty() && tripId.isNotEmpty()) {
            lifecycleScope.launch {
                pref.getToken().collect { token ->
                    if (token.isNotEmpty()) {
                        itineraryViewModel.getIteneraries(token, tripId, itineraryId)
                    } else {
                        Toast.makeText(this@IteneraryActivity, "Token is empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Invalid itinerary or trip ID", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupUI() {
        adapter = IteneraryAdapter(emptyList())
        binding.rvItineraries.layoutManager = LinearLayoutManager(this)
        binding.rvItineraries.adapter = adapter
    }

    private fun observeViewModel() {
        itineraryViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        itineraryViewModel.iteneraries.observe(this) { itineraries ->
            if (itineraries.isNullOrEmpty()) {
                binding.tvNoItineraries.visibility = View.VISIBLE
                binding.rvItineraries.visibility = View.GONE
            } else {
                binding.tvNoItineraries.visibility = View.GONE
                binding.rvItineraries.visibility = View.VISIBLE
                adapter.updateData(itineraries)
            }
        }

        itineraryViewModel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, itineraryViewModel.message.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
}