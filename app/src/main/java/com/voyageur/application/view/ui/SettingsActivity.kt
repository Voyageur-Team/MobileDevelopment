package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.voyageur.application.databinding.ActivitySettingsBinding
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth

        val user = firebaseAuth.currentUser
        user?.let {
            binding.textView25.text = it.displayName
            binding.textView26.text = it.email

            it.photoUrl?.let { photoUrl ->
                Glide.with(this)
                    .load(photoUrl)
                    .into(binding.imageView5)
            }
        }

        binding.btnLogout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@SettingsActivity)
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
            finish()
        }
    }
}