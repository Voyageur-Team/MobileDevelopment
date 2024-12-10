package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.R
import com.voyageur.application.data.adapter.VotingAdapter
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityVotingBinding
import com.voyageur.application.viewmodel.DetailTripViewModel
import com.voyageur.application.viewmodel.VotingViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class VotingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVotingBinding
    private val votingViewModel: VotingViewModel by viewModels()
    private lateinit var pref: AppPreferences
    private var tripId: String? = null
    private lateinit var adapter: VotingAdapter
    private val detailTrip: DetailTripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVotingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
                        detailTrip.getTripDetail(token, tripId!!)
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

        displayEventDate()
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
                            binding.btnVote.visibility = View.GONE
                        }
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun displayEventDate() {
        val tripStartDateString = detailTrip.tripDetail.value?.tripStartDate
        if (tripStartDateString != null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val tripStartDate: Date? = dateFormat.parse(tripStartDateString)

            if (tripStartDate != null) {
                val calendar = Calendar.getInstance()
                calendar.time = tripStartDate
                calendar.add(Calendar.DAY_OF_YEAR, -1)

                val eventDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val eventDate = eventDateFormat.format(calendar.time)

                binding.tvDate.text = eventDate
            } else {
                binding.tvDate.text = "Invalid trip start date"
            }
        } else {
            binding.tvDate.text = "Trip start date not available"
        }
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
