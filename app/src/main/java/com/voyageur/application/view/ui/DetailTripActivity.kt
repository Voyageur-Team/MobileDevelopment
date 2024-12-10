package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.voyageur.application.data.model.DataTrip
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

        Handler(Looper.getMainLooper()).postDelayed({
            setupUI()
        },2000)
        observeViewModel()

        binding.swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                setupUI()
            },2000)
        }
    }

    private fun setupUI() {
        binding.btnRekomendasi.visibility = View.GONE
        showLoading(true)

        lifecycleScope.launch {
            val token = pref.getToken().firstOrNull()
            userId = pref.getUserId().firstOrNull()

            if (token != null && userId != null && tripId != null) {
                detailTripViewModel.getTripDetail(tripId!!, token)

                detailTripViewModel.tripDetail.observe(this@DetailTripActivity) { tripDetail ->
                    checkUserIdEqualsCreatedBy(tripDetail)
                    detailTripViewModel.getSizeParticipants(tripId!!, token)
                    detailTripViewModel.checkUserAlreadyVoting(token, tripId!!, userId!!)
                    detailTripViewModel.getIteneraryUserId(token, tripId!!, userId!!)
                    showLoading(false)
                }
            } else {
                showLoading(false)
                Toast.makeText(this@DetailTripActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            val token = pref.getToken().firstOrNull()
            if (token != null && tripId != null) {
                detailTripViewModel.calculateParticipantProgress(token, tripId!!)
            } else {
                Toast.makeText(this@DetailTripActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRekomendasi.setOnClickListener {
            showLoading(true)
            Toast.makeText(this@DetailTripActivity, "Harap tunggu, sedang memproses rekomendasi", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                lifecycleScope.launch {
                    val token = pref.getToken().firstOrNull()
                    if (token != null && tripId != null) {
                        detailTripViewModel.postMostPreferences(token, tripId!!)
                        detailTripViewModel.postRecommendations(token, tripId!!)
                        detailTripViewModel.getTripDetail(tripId!!, token)
                        showLoading(false)
                        Toast.makeText(this@DetailTripActivity, "Rekomendasi berhasil dibuat", Toast.LENGTH_SHORT).show()


                    } else {
                        showLoading(false)
                        Toast.makeText(this@DetailTripActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }, 5000)
        }

        binding.makeTrip.setOnClickListener {
            val intent = Intent(this, MakeTripActivity::class.java)
            intent.putExtra("TRIP_ID", tripId)
            startActivity(intent)
        }

        binding.btnHasil.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("TRIP_ID", tripId)
            startActivity(intent)
        }

        binding.btnHasilRekomendasi.setOnClickListener {
            if (detailTripViewModel.userVote.value == false) {
                val intent = Intent(this@DetailTripActivity, IteneraryActivity::class.java).apply {
                    putExtra("ITINERARY_ID", detailTripViewModel.iteneraryUser.value?.idItenerary)
                    putExtra("TRIP_ID", tripId)
                }
                startActivity(intent)
            } else {
                val intent = Intent(this@DetailTripActivity, VotingActivity::class.java).apply {
                    putExtra("TRIP_ID", tripId)
                }
                startActivity(intent)
            }
        }
    }


    private fun checkUserIdEqualsCreatedBy(trip: DataTrip) {
        if (userId == trip.createdBy ) {
            binding.btnRekomendasi.visibility = View.VISIBLE
        } else {
            binding.btnRekomendasi.visibility = View.GONE
        }
    }

    private fun observeViewModel() {
        detailTripViewModel.participantsCount.observe(this) { count ->
            binding.tvAnggota.text = "$count Orang"
        }

        detailTripViewModel.completedParticipants.observe(this) { completed ->
            val total = detailTripViewModel.totalParticipants.value ?: 0
            binding.progressParticipants.text = "$completed/$total"
            updateButtonVisibility(completed, total)
        }

        detailTripViewModel.totalParticipants.observe(this) { total ->
            val completed = detailTripViewModel.completedParticipants.value ?: 0
            binding.progressParticipants.text = "$completed/$total"
            updateButtonVisibility(completed, total)
        }

        detailTripViewModel.tripDetail.observe(this) { tripDetail ->
            binding.tvTitle.text = tripDetail.title
            binding.tvDescription.text = "${tripDetail.mostCommonCategories?.get(0)}, ${tripDetail.mostCommonCategories?.get(1)}, ${tripDetail.mostCommonCategories?.get(2)}"
            binding.tvCity.text = tripDetail.mostCommonDestination ?: "Belum ditentukan"
            binding.tvBudget.text = tripDetail.averageBudgetRange?.let { formatRupiah(it.toDouble()) }
                ?: "Belum ditentukan"
            binding.tvStartDate.text = tripDetail.tripStartDate ?: "Belum ditentukan"
            binding.tvEndDate.text = tripDetail.tripEndDate ?: "Belum ditentukan"
        }

        detailTripViewModel.isLoading.observe(this) { isLoading ->
            if (!isLoading) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateButtonVisibility(completed: Int, total: Int) {
        if (completed == total) {
            binding.btnRekomendasi.visibility = View.VISIBLE
        } else {
            binding.btnRekomendasi.visibility = View.GONE
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
