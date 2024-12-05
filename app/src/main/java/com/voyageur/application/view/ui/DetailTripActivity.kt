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
import com.voyageur.application.viewmodel.DetailTripViewModel
import kotlinx.coroutines.launch

class DetailTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTripBinding
    private lateinit var pref: AppPreferences
    private val detailTripViewModel: DetailTripViewModel by viewModels()
    private var tripId: String? = null

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

        if (tripId != null) {
            observeViewModel()
            lifecycleScope.launch {
                pref.getToken().collect { token ->
                    detailTripViewModel.getSizeParticipants(tripId!!, token)
                }
            }
        } else {
            Toast.makeText(this, "Trip ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnRekomendasi.setOnClickListener {
            startActivity(Intent(this, VotingActivity::class.java))
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

        detailTripViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        detailTripViewModel.isError.observe(this) { isError ->
            if (isError) {
                binding.tvAnggota.text = detailTripViewModel.message.value
            }
        }
    }
}

