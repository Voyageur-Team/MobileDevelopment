package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.R
import com.voyageur.application.data.adapter.InviteAdapterParticipants
import com.voyageur.application.data.adapter.InviteAdapterUsers
import com.voyageur.application.data.model.Participants
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityInviteBinding
import com.voyageur.application.viewmodel.InviteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InviteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInviteBinding
    private lateinit var adapterParticipants: InviteAdapterParticipants
    private lateinit var adapterUsers: InviteAdapterUsers
    private val inviteViewModel: InviteViewModel by viewModels()
    private lateinit var pref: AppPreferences

    private var tripId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Undang Temanmu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AppPreferences.getInstance(applicationContext.dataStore)

        tripId = intent.getStringExtra("TRIP_ID")
        if (tripId != null) {
            lifecycleScope.launch {
                pref.getToken().collect { token ->
                    if (token.isNotEmpty()) {
                        inviteViewModel.fetchTripDetail(tripId!!, token)
                        setupRecyclerViewParticipants()
                        setupRecyclerViewUsers()
                        observeViewModelParticipants()
                        observeViewModelUsers()
                    }
                }
            }
        } else {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            val email = binding.etSearchUserByEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                tripId?.let {
                    lifecycleScope.launch {
                        pref.getToken().collect { token ->
                            if (token.isNotEmpty()) {
                                inviteViewModel.searchUserByEmail(email, token)
                            }
                        }
                    }
                }
            } else {
                adapterUsers.updateUsers(emptyList())
            }
        }


        binding.swipeRefreshLayoutParticipants.setOnRefreshListener {
            refreshParticipants()
        }

        binding.btnLanjut.setOnClickListener {
            val intent = Intent(this, DetailTripActivity::class.java)
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

    private fun setupRecyclerViewParticipants() {
        adapterParticipants = InviteAdapterParticipants(emptyList())
        binding.rvAnggota.layoutManager = LinearLayoutManager(this)
        binding.rvAnggota.adapter = adapterParticipants
    }

    private fun setupRecyclerViewUsers() {
        adapterUsers = InviteAdapterUsers(emptyList()) { user ->
            tripId?.let { id ->
                lifecycleScope.launch {
                    pref.getToken().collect { token ->
                        if (token.isNotEmpty()) {
                            addParticipantToTrip(id, user.userId, user.userName, user.email, token)
                        }
                    }
                }
            }
        }
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapterUsers
    }

    private fun observeViewModelParticipants() {
        inviteViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        inviteViewModel.participants.observe(this) { participants ->
            adapterParticipants.updateParticipants(participants)
        }
    }

    private fun observeViewModelUsers() {
        inviteViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        inviteViewModel.userEmail.observe(this) { users ->
            if (users.isEmpty()) {
                adapterUsers.updateUsers(emptyList())
                Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show()
            } else {
                adapterUsers.updateUsers(users)
                users.forEach { user ->
                    Log.d("InviteActivity", "User data: userId=${user.userId}, userName=${user.userName}, email=${user.email}")
                }
            }
        }
    }

    private fun refreshParticipants() {
        lifecycleScope.launch {
            pref.getToken().collect() { token ->
                if (token.isNotEmpty()) {
                    tripId?.let {
                        inviteViewModel.fetchTripDetail(it, token)
                        binding.swipeRefreshLayoutParticipants.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun addParticipantToTrip(tripId: String, userId: String?, userName: String?, email: String?, token: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Add Participant")
        dialogBuilder.setMessage("Are you sure you want to add $userName as a participant to the trip?")

        dialogBuilder.setPositiveButton("Yes") { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                if (userId.isNullOrEmpty() || userName.isNullOrEmpty() || email.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@InviteActivity, "User data is incomplete", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                val participant = Participants(userName, userId, email)
                inviteViewModel.addParticipantsToTrip(tripId, participant, token)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@InviteActivity, "Participant added successfully", Toast.LENGTH_SHORT).show()
                    inviteViewModel.fetchTripDetail(tripId, token)

                    inviteViewModel.participants.observe(this@InviteActivity) { updatedParticipants ->
                        adapterParticipants.updateParticipants(updatedParticipants)
                    }
                }
            }
        }

        dialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
