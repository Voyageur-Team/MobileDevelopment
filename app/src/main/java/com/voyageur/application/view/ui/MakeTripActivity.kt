package com.voyageur.application.view.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.voyageur.application.R
import com.voyageur.application.data.adapter.MakeTripAdapter
import com.voyageur.application.data.model.DataParticipantPreferences
import com.voyageur.application.data.model.Preferences
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityMakeTripBinding
import com.voyageur.application.viewmodel.MakeTripViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MakeTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMakeTripBinding
    private val makeTripViewModel: MakeTripViewModel by viewModels()
    private lateinit var pref: AppPreferences
    private lateinit var preferencesAdapter: MakeTripAdapter
    private val selectedPreferences = mutableListOf<Preferences>()
    private var tripId: String? = null

    private var userToken: String? = null
    private var userId: String? = null
    private var userName: String? = null
    private var userEmail: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeTripBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = "Buat Rencana Bersama"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        pref = AppPreferences.getInstance(applicationContext.dataStore)
        tripId = intent.getStringExtra("TRIP_ID")
        setupTokenObserver()
        setupRecyclerView()
        observeViewModelCities()
        observeViewModelPreferences()
        fetchCities()
        fetchPreferences()

        pref.getUserId().asLiveData().observe(this) { id ->
            userId = id ?: ""
            if (!tripId.isNullOrEmpty() && !userId.isNullOrEmpty()) {
                lifecycleScope.launch {
                    val token = pref.getToken().firstOrNull()
                    if (!token.isNullOrEmpty()) {
                        loadTripDetail(token, tripId!!, userId!!)
                    }
                }
            }
        }

        binding.datePicker.setOnClickListener {
            openDateRangePicker()
        }

        binding.btnSimpan.setOnClickListener {
            val tujuan1 = binding.etTujuan1.text.toString()
            val tujuan2 = binding.etTujuan2.text.toString()
            val date1 = binding.datePicker.text.toString().split(" - ").getOrNull(0)
            val date2 = binding.datePicker.text.toString().split(" - ").getOrNull(1)
            val budgetMin = binding.budgetMin.text.toString()
            val budgetMax = binding.budgetMax.text.toString()
            val preferences = selectedPreferences.map { it.name }

            if (userToken.isNullOrEmpty() || userId.isNullOrEmpty() || userName.isNullOrEmpty() || userEmail.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Data user tidak ditemukan. Pastikan kamu sudah login.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (validateInput(tujuan1, tujuan2, date1, date2, budgetMin, budgetMax, preferences)) {
                val createPreferences = DataParticipantPreferences(
                    preferredCategory = preferences,
                    budgetRange = listOf(budgetMin.toInt(), budgetMax.toInt()),
                    preferredDestinations = listOf(tujuan1, tujuan2),
                    userName = userName!!,
                    userId = userId!!,
                    availableDates = listOfNotNull(date1, date2),
                    email = userEmail!!
                )

                if (tripId.isNullOrEmpty()) {
                    Toast.makeText(this, "ID Trip tidak ditemukan.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                makeTripViewModel.postParticipantPreferences(
                    userToken!!,
                    tripId!!,
                    userId!!,
                    createPreferences
                )
            }
            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun validateInput(
        tujuan1: String,
        tujuan2: String,
        date1: String?,
        date2: String?,
        budgetMin: String,
        budgetMax: String,
        preferences: List<String>
    ): Boolean {
        if (tujuan1.isEmpty() || tujuan2.isEmpty() || date1.isNullOrEmpty() || date2.isNullOrEmpty() || budgetMin.isEmpty() || budgetMax.isEmpty() || preferences.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
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
            } else {
                Log.d("MakeTripActivity", "tripId: $tripId, userToken: $userToken, userId: $userId, userName: $userName, userEmail: $userEmail")
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

    private fun fetchCities() {
        lifecycleScope.launch {
            pref.getToken().collect { token ->
                if (token.isNotEmpty()) {
                    makeTripViewModel.getAllCities(token)
                }
            }
        }
    }

    private fun fetchPreferences() {
        lifecycleScope.launch {
            pref.getToken().collect { token ->
                if (token.isNotEmpty()) {
                    makeTripViewModel.getAllPreferences(token)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        preferencesAdapter = MakeTripAdapter(emptyList(), { preference ->
            if (selectedPreferences.size < 3) {
                selectedPreferences.add(preference)
                val selectedNames = selectedPreferences.joinToString(", ") { it.name }
                binding.preferenceCategory.setText(selectedNames)
            } else {
                Toast.makeText(this, "Maksimal 3 preferensi yang dapat dipilih", Toast.LENGTH_SHORT).show()
            }
        }, { preference ->
            selectedPreferences.remove(preference)
            val selectedNames = selectedPreferences.joinToString(", ") { it.name }
            binding.preferenceCategory.setText(selectedNames)
        })
        binding.rvPreferences.apply {
            layoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
            adapter = preferencesAdapter
        }
    }

    private fun observeViewModelCities() {
        makeTripViewModel.cities.observe(this) { cities ->
            val cityNames = cities.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cityNames)
            binding.etTujuan1.setAdapter(adapter)
            binding.etTujuan2.setAdapter(adapter)
        }

        makeTripViewModel.isError.observe(this) { isError ->
            if (isError) {
                val message = makeTripViewModel.message.value
                if (message != null) {
                    println("Error: $message")
                } else {
                    println("Error: Unknown error occurred")
                }
            }
        }

        makeTripViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility =
                if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    private fun observeViewModelPreferences() {
        makeTripViewModel.preferences.observe(this) { preferencesList ->
            if (preferencesList.isNotEmpty()) {
                preferencesAdapter = MakeTripAdapter(preferencesList, { preference ->
                    if (selectedPreferences.size < 3) {
                        selectedPreferences.add(preference)
                        val selectedNames = selectedPreferences.joinToString(", ") { it.name }
                        binding.preferenceCategory.setText(selectedNames)
                    } else {
                        Toast.makeText(this, "Maksimal 3 preferensi yang dapat dipilih", Toast.LENGTH_SHORT).show()
                    }
                }, { preference ->
                    selectedPreferences.remove(preference)
                    val selectedNames = selectedPreferences.joinToString(", ") { it.name }
                    binding.preferenceCategory.setText(selectedNames)
                })
                binding.rvPreferences.adapter = preferencesAdapter
            } else {
                println("No preferences found")
            }
        }

        makeTripViewModel.isError.observe(this) { isError ->
            if (isError) {
                println("Error fetching preferences")
            }
        }

        makeTripViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    private fun openDateRangePicker() {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraints)
            .setTitleText("Pilih Rentang Tanggal")
            .build()

        dateRangePicker.show(supportFragmentManager, dateRangePicker.toString())
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection?.first
            val endDate = selection?.second
            val formattedStartDate = formatDate(startDate)
            val formattedEndDate = formatDate(endDate)
            binding.datePicker.setText("$formattedStartDate - $formattedEndDate")
        }

        dateRangePicker.addOnNegativeButtonClickListener {
            Toast.makeText(this, "Pilih Rentang Tanggal Dibatalkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadTripDetail(token: String, tripId: String, userId: String) {
        makeTripViewModel.getUserPreffered(token, tripId, userId)
        makeTripViewModel.tripDetailUser.observe(this) { tripDetail ->
            if (tripDetail != null) {
                binding.etTujuan1.setText(tripDetail.preferredDestinations[0])
                binding.etTujuan2.setText(tripDetail.preferredDestinations[1])
                val startDate = tripDetail.availableDates[0]
                val endDate = tripDetail.availableDates[1]
                binding.datePicker.setText("$startDate - $endDate")
                binding.budgetMin.setText(tripDetail.budgetRange[0].toString())
                binding.budgetMax.setText(tripDetail.budgetRange[1].toString())
                binding.preferenceCategory.setText(tripDetail.preferredCategory.joinToString(", "))
            } else {
                Log.e("MakeTripActivity", "Trip detail is null")
            }
        }
    }

    private fun formatDate(timestamp: Long?): String {
        timestamp?.let {
            val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            return format.format(java.util.Date(it))
        }
        return ""
    }
}