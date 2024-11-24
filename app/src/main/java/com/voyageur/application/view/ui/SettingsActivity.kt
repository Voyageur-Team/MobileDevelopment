package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivitySettingsBinding
import com.voyageur.application.viewmodel.TokenViewModel
import com.voyageur.application.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var pref: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        pref = AppPreferences.getInstance(applicationContext.dataStore)

        setUserData()

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun setUserData() {
        val userName: String
        runBlocking {
            userName = pref.getName().first()
        }

        binding.tvName.text = userName
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