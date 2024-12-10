package com.voyageur.application.view.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.R
import com.voyageur.application.data.adapter.RecommendationAdapterDays
import com.voyageur.application.data.adapter.RecommendationAdapterIteneraries
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityRecommendationBinding
import com.voyageur.application.viewmodel.RecommendationViewModel
import kotlinx.coroutines.launch

class RecommendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationBinding
    private lateinit var adapterIteneraries: RecommendationAdapterIteneraries
    private lateinit var adapterDays: RecommendationAdapterDays
    private lateinit var tripId: String
    private lateinit var pref: AppPreferences
    private val recommendationViewModel: RecommendationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Rekomendasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AppPreferences.getInstance(applicationContext.dataStore)
        tripId = intent.getStringExtra("TRIP_ID") ?: ""

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchData()
        }

        setupUI()
        observeViewModel()
        fetchData()
    }

    private fun setupUI() {
        adapterIteneraries = RecommendationAdapterIteneraries(emptyList())
        binding.rvIteneraries.layoutManager = LinearLayoutManager(this)
        binding.rvIteneraries.adapter = adapterIteneraries

        adapterDays = RecommendationAdapterDays(emptyList()) { day ->
            filterItemsByDay(day)
        }
        binding.rvDays.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDays.adapter = adapterDays
    }

    private fun observeViewModel() {
        recommendationViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        recommendationViewModel.recommendations.observe(this) { itineraries ->
            if (itineraries.isNullOrEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvIteneraries.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rvIteneraries.visibility = View.VISIBLE
                adapterIteneraries.updateData(itineraries)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

        recommendationViewModel.days.observe(this) { days ->
            if (days.isNullOrEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvDays.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rvDays.visibility = View.VISIBLE
                adapterDays.updateData(days)
                filterItemsByDay(1)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun fetchData() {
        if (tripId.isNotEmpty()) {
            lifecycleScope.launch {
                pref.getToken().collect { token ->
                    if (token.isNotEmpty()) {
                        recommendationViewModel.getRecommendations(token, tripId)
                    }
                }
            }
        } else {
            finish()
        }
    }

    private fun filterItemsByDay(day: Int) {
        val filteredItems =
            recommendationViewModel.days.value?.find { it.day == day }?.items ?: emptyList()
        adapterIteneraries.updateData(filteredItems)
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