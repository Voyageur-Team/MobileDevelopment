package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.voyageur.application.R
import com.voyageur.application.data.adapter.OnboardingAdapter
import com.voyageur.application.databinding.ActivityOnboardingBinding

@Suppress("DEPRECATION")
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private var dots: Array<TextView?> = arrayOfNulls(4)
    private var onBoardingAdapter: OnboardingAdapter? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth

        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding.skipButton.setOnClickListener {
            val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        onBoardingAdapter = OnboardingAdapter(this)
        binding.slideViewPager.adapter = onBoardingAdapter

        setUpindicator(0)
        binding.slideViewPager.addOnPageChangeListener(viewListener)
    }

    private fun setUpindicator(position: Int) {
        dots = arrayOfNulls(4)
        binding.indicatorLayout.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226")
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(resources.getColor(R.color.colorSecondary, theme))
            binding.indicatorLayout.addView(dots[i])
        }

        dots[position]?.setTextColor(resources.getColor(R.color.colorPrimary, theme))
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            setUpindicator(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}
