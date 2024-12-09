package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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

        binding.btnVote.setOnClickListener {
            val selectedItinerary = adapter.getSelectedItinerary()
            if (selectedItinerary != null) {
                showConfirmationDialog(selectedItinerary.itineraryId)
            } else {
                Toast.makeText(this@VotingActivity, "Please select an itinerary", Toast.LENGTH_SHORT).show()
            }
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

    private fun showConfirmationDialog(itineraryId: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Vote")
            .setMessage("Are you sure you want to vote for this recommendation?")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    pref.getUserId().collect { userId ->
                        pref.getToken().collect { token ->
                            if (token.isNullOrEmpty()) {
                                Toast.makeText(this@VotingActivity, "Token is empty", Toast.LENGTH_SHORT).show()
                                return@collect
                            }
                            if (userId.isNullOrEmpty()) {
                                Toast.makeText(this@VotingActivity, "User ID is empty", Toast.LENGTH_SHORT).show()
                                return@collect
                            }
                            votingViewModel.userVote(token, tripId!!, userId, itineraryId)
                            Toast.makeText(this@VotingActivity, "Voting berhasil", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
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