package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.voyageur.application.R
import com.voyageur.application.data.adapter.OnboardingAdapter
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Suppress("DEPRECATION")
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var pref: AppPreferences
    private var dots: Array<TextView?> = arrayOfNulls(4)
    private var onBoardingAdapter: OnboardingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        supportActionBar?.hide()

        pref = AppPreferences.getInstance(applicationContext.dataStore)

        checkToken()

        binding.skipButton.setOnClickListener {
            val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        onBoardingAdapter = OnboardingAdapter(this)
        binding.slideViewPager.adapter = onBoardingAdapter

        setUpIndicator(0)
        binding.slideViewPager.addOnPageChangeListener(viewListener)
    }

    private fun checkToken() {
        val token = runBlocking { pref.getToken().first() }
        if (token.isNotEmpty()) {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setUpIndicator(position: Int) {
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
            setUpIndicator(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}
