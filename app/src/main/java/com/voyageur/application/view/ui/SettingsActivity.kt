package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.voyageur.application.R
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivitySettingsBinding
import com.voyageur.application.viewmodel.PlansViewModel
import com.voyageur.application.viewmodel.TokenViewModel
import com.voyageur.application.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var pref: AppPreferences
    private val plansViewModel: PlansViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = "Pengaturan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AppPreferences.getInstance(applicationContext.dataStore)

        setUserData()

        binding.btnAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        plansViewModel.trips.observe(this) {
            if (it.isNotEmpty()) {
                binding.textView28.text = it.size.toString()
            }
        }

        lifecycleScope.launch{
            val token = pref.getToken().first()
            val userId = pref.getUserId().first()
            if(userId.isNotEmpty() && token.isNotEmpty()){
                plansViewModel.getAllTripsUserId(userId, token)
            }
        }

    }

    private fun setUserData() {
        val userName: String
        runBlocking {
            userName = pref.getName().first()
        }

        val email: String
        runBlocking {
            email = pref.getEmail().first()
        }

        binding.tvName.text = userName
        binding.textView8.text = email
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

    private fun logoutUser(){
        val dialog = AlertDialog.Builder(this)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[TokenViewModel::class.java]
        dialog.setTitle("Logout")
        dialog.setMessage("Apakah anda yakin ingin keluar dari akun?")
        dialog.setPositiveButton("Ya") { _, _ ->
            mainViewModel.clearDataLogin()
            Toast.makeText(this, "Logout Sukses", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        dialog.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }
}