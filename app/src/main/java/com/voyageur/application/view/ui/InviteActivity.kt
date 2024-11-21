package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityInviteBinding

class InviteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInviteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLanjut.setOnClickListener {
            startActivity(Intent(this, DetailTripActivity::class.java))
        }

    }
}