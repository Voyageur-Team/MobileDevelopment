package com.voyageur.application.view.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val splashScreenLogo = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(3000)

        AnimatorSet().apply {
            playSequentially(splashScreenLogo)
            start()
        }

        binding.imageView.animate()
            .setDuration(5000)
            .alpha(0f)
            .withEndAction {
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
    }
}