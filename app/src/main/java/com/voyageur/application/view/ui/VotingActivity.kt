package com.voyageur.application.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivityVotingBinding

class VotingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVotingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVotingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

    }
}