package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.data.adapter.VotingAdapter
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityVotingBinding
import com.voyageur.application.viewmodel.VotingViewModel
import kotlinx.coroutines.launch

class VotingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVotingBinding
    private val votingViewModel: VotingViewModel by viewModels()
    private lateinit var pref: AppPreferences
    private var tripId: String? = null

    private lateinit var adapter: VotingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVotingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Voting"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AppPreferences.getInstance(applicationContext.dataStore)
        tripId = intent.getStringExtra("TRIP_ID")
        setupUI()
        observeViewModel()

        if (tripId != null) {
            observeViewModel()
            lifecycleScope.launch {
                pref.getToken().collect { token ->
                    if (token.isNullOrEmpty()) {
                        Toast.makeText(this@VotingActivity, "Token is empty", Toast.LENGTH_SHORT).show()
                        return@collect
                    }
                    votingViewModel.getRecommendations(token, tripId!!)
                }
            }
        } else {
            finish()
        }
    }

    private fun setupUI() {
        adapter = VotingAdapter(emptyList()) { itinerary ->
            val intent = Intent(this, IteneraryActivity::class.java).apply {
                putExtra("ITINERARY_ID", itinerary.itineraryId)
                putExtra("TRIP_ID", tripId)
            }
            startActivity(intent)
        }
        binding.rvIteneraries.layoutManager = LinearLayoutManager(this)
        binding.rvIteneraries.adapter = adapter
    }

    private fun observeViewModel() {
        votingViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        votingViewModel.itineraries.observe(this) { itineraries ->
            adapter.updateData(itineraries)
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