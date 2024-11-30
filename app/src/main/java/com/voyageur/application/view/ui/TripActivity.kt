// TripActivity.kt
package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import com.voyageur.application.R
import com.voyageur.application.data.model.CreateTrip
import com.voyageur.application.data.model.Participants
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityTripBinding
import com.voyageur.application.viewmodel.TripViewModel

class TripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTripBinding
    private lateinit var pref: AppPreferences
    private val tripViewModel: TripViewModel by viewModels()

    private var userToken: String? = null
    private var userId: String? = null
    private var userName: String? = null
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = "Buat Trip"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AppPreferences.getInstance(applicationContext.dataStore)
        setupTokenObserver()
        setupObservers()

        binding.progressBar.visibility = View.GONE

        binding.btnCreate.setOnClickListener {
            val title = binding.etNama.text?.toString()?.trim()
            val duration = binding.etDuration.text?.toString()?.toIntOrNull()
            val description = binding.etDeskripsi.text?.toString()?.trim()

            if (userToken.isNullOrEmpty() || userId.isNullOrEmpty() || userName.isNullOrEmpty() || userEmail.isNullOrEmpty()) {
                Log.e("TripActivity", "Missing user data: userToken=$userToken, userId=$userId, userName=$userName, userEmail=$userEmail")
                Toast.makeText(this, "Data user tidak ditemukan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (validateInput(title, duration, description)) {
                val participants = listOf(
                    Participants(userId = userId!!, userName = userName!!, email = userEmail!!)
                )
                val createTrip = CreateTrip(
                    title = title!!,
                    duration = duration!!,
                    description = description!!,
                    participants = participants
                )

                tripViewModel.createTrip(createTrip, userToken!!)
            }
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

    private fun validateInput(title: String?, duration: Int?, description: String?): Boolean {
        if (title.isNullOrEmpty()) {
            binding.etNama.error = "Nama trip tidak boleh kosong."
            return false
        }
        if (duration == null || duration <= 0) {
            binding.etDuration.error = "Durasi harus berupa angka lebih besar dari 0."
            return false
        }
        if (description.isNullOrEmpty()) {
            binding.etDeskripsi.error = "Deskripsi tidak boleh kosong."
            return false
        }
        return true
    }

    private fun setupTokenObserver() {
        pref.getToken().asLiveData().observe(this) { token ->
            userToken = token ?: ""
        }
        pref.getName().asLiveData().observe(this) { name ->
            userName = name ?: "Anonymous"
        }
        pref.getUserId().asLiveData().observe(this) { id ->
            userId = id ?: ""
        }
        pref.getEmail().asLiveData().observe(this) { email ->
            userEmail = email ?: ""
        }

        pref.getLoginSession().asLiveData().observe(this) { session ->
            if (session == false) {
                Toast.makeText(this, "Sesi login tidak aktif.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupObservers() {
        tripViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnCreate.isEnabled = !isLoading
        }

        tripViewModel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, tripViewModel.message.value, Toast.LENGTH_SHORT).show()
            }
        }

        tripViewModel.message.observe(this) { message ->
            if (!tripViewModel.isError.value!!) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val tripId = tripViewModel.trip.value?.data?.id
                tripId?.let {
                    val intent = Intent(this, InviteActivity::class.java).apply {
                        putExtra("TRIP_ID", it)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}