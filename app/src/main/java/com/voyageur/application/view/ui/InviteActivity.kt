package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.data.adapter.InviteAdapter
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityInviteBinding
import com.voyageur.application.viewmodel.InviteViewModel
import kotlinx.coroutines.launch

class InviteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInviteBinding
    private lateinit var inviteAdapter: InviteAdapter
    private val inviteViewModel: InviteViewModel by viewModels()
    private lateinit var pref: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        pref = AppPreferences.getInstance(applicationContext.dataStore)

        val tripId = intent.getStringExtra("TRIP_ID")
        if (tripId != null) {
            lifecycleScope.launch {
                pref.getToken().collect { token ->
                    if (token.isNotEmpty()) {
                        inviteViewModel.fetchTripDetail(tripId, token)
                        setupRecyclerView()
                        observeViewModel()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Trip ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnLanjut.setOnClickListener {
            startActivity(Intent(this, DetailTripActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        inviteAdapter = InviteAdapter(emptyList())
        binding.rvAnggota.layoutManager = LinearLayoutManager(this)
        binding.rvAnggota.adapter = inviteAdapter
    }

    private fun observeViewModel() {
        inviteViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        inviteViewModel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, inviteViewModel.message.value, Toast.LENGTH_SHORT).show()
            }
        }

        inviteViewModel.participants.observe(this) { participants ->
            inviteAdapter.updateParticipants(participants)
        }
    }
}
