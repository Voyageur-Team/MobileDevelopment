package com.voyageur.application.view.ui

import android.content.Intent
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
import com.voyageur.application.R
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityDetailTripBinding
import com.voyageur.application.view.utils.Formatted.Companion.formatRupiah
import com.voyageur.application.viewmodel.DetailTripViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DetailTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTripBinding
    private lateinit var pref: AppPreferences
    private val detailTripViewModel: DetailTripViewModel by viewModels()
    private var tripId: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTripBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AppPreferences.getInstance(applicationContext.dataStore)
        tripId = intent.getStringExtra("TRIP_ID")


        setupUI()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (tripId != null) {
            lifecycleScope.launch {
                val token = pref.getToken().firstOrNull()
                userId = pref.getUserId().firstOrNull()
                if (token != null && userId != null) {
                    detailTripViewModel.getSizeParticipants(tripId!!, token)
                    detailTripViewModel.getTripDetail(tripId!!, token)
                    detailTripViewModel.checkUserAlreadyVoting(token, tripId!!, userId!!)
                    detailTripViewModel.getIteneraryUserId(token, tripId!!, userId!!)
                } else {
                    Toast.makeText(this@DetailTripActivity, "Token or User ID not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Trip ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupUI() {
        binding.btnPrefences.setOnClickListener {
            lifecycleScope.launch {
                val token = pref.getToken().firstOrNull()
                if (token != null && tripId != null) {
                    detailTripViewModel.postMostPreferences(token, tripId!!)
                    detailTripViewModel.postRecommendations(token, tripId!!)
                } else {
                    Toast.makeText(this@DetailTripActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRekomendasi.setOnClickListener {
            if (detailTripViewModel.userVote.value == false) {
                val intent = Intent(this, IteneraryActivity::class.java).apply {
                    putExtra("ITINERARY_ID", detailTripViewModel.iteneraryUser.value?.idItenerary)
                    putExtra("TRIP_ID", tripId)
                }
                startActivity(intent)
            } else {
                val intent = Intent(this, VotingActivity::class.java).apply {
                    putExtra("TRIP_ID", tripId)
                }
                startActivity(intent)
            }
        }

        binding.makeTrip.setOnClickListener {
            val intent = Intent(this, MakeTripActivity::class.java)
            intent.putExtra("TRIP_ID", tripId)
            startActivity(intent)
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

    private fun observeViewModel() {
        detailTripViewModel.participantsCount.observe(this) { count ->
            binding.tvAnggota.text = "$count Orang"
        }

        detailTripViewModel.tripDetail.observe(this) { tripDetail ->
            binding.tvTitle.text = tripDetail.title
            binding.tvDescription.text = tripDetail.description
            binding.tvCity.text = tripDetail.mostCommonDestination ?: "Belum ditentukan"
            binding.tvBudget.text = tripDetail.averageBudgetRange?.let { formatRupiah(it.toDouble()) }
                ?: "Belum ditentukan"
            binding.tvStartDate.text = tripDetail.tripStartDate ?: "Belum ditentukan"
            binding.tvEndDate.text = tripDetail.tripEndDate ?: "Belum ditentukan"
            binding.progressParticipants.text = tripDetail.participants.size.toString()
        }

        detailTripViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        detailTripViewModel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, detailTripViewModel.message.value, Toast.LENGTH_SHORT).show()
            }
        }
    }
}