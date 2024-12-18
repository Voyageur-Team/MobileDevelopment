package com.voyageur.application.view.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.voyageur.application.R
import com.voyageur.application.data.model.LoginDataAccount
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.ActivityLoginBinding
import com.voyageur.application.viewmodel.AuthViewModel
import com.voyageur.application.viewmodel.TokenViewModel
import com.voyageur.application.viewmodel.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private var isLoginInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()

        showLoading(false)
        setupSpannableString()
        setupObservers()
        setAlphaToZero()
        setupAnimation()

        binding.btnLogin.setOnClickListener {
            binding.etEmail.clearFocus()
            binding.etPassword.clearFocus()

            if (checkAkun()) {
                if (isLoginInProgress) return@setOnClickListener

                showLoading(true)
                isLoginInProgress = true

                val requestLogin = LoginDataAccount(
                    binding.etEmail.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                )

                loginViewModel.login(requestLogin)
            } else {
                showLoginError()
            }
        }

        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun setupSpannableString() {
        val text = getString(R.string.belum_akun)
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

        val startIndex = text.indexOf("Register")
        val endIndex = startIndex + "Register".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvAskAkun.text = spannableString
        binding.tvAskAkun.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupObservers() {
        val preferences = AppPreferences.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[TokenViewModel::class.java]

        mainViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                navigateToApp()
            }
        }

        loginViewModel.isLoading.observe(this) { showLoading(it) }
        loginViewModel.message.observe(this) { response ->
            response?.let {
                checkLogin(mainViewModel)
            }
        }
    }

    private fun checkLogin(userLoginViewModel: TokenViewModel) {
        loginViewModel.isError.observe(this) { isError ->
            if (!isError) {
                val user = loginViewModel.userLogin.value
                if (user != null) {
                    userLoginViewModel.saveLoginSession(true)
                    userLoginViewModel.saveToken(user.loginResult.token)
                    userLoginViewModel.saveName(user.loginResult.userName)
                    userLoginViewModel.saveUserId(user.loginResult.userId)
                    userLoginViewModel.saveEmail(user.loginResult.email)
                } else {
                    Toast.makeText(this, "Data pengguna tidak lengkap.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, loginViewModel.message.value, Toast.LENGTH_SHORT).show()
            }

            isLoginInProgress = false
        }
    }

    private fun checkAkun(): Boolean {
        return binding.etEmail.isEmailValid && binding.etPassword.isPasswordValid
    }

    private fun showLoginError() {
        if (!binding.etEmail.isEmailValid) binding.etEmail.error = getString(R.string.email_tidak_ada)
        if (!binding.etPassword.isPasswordValid) binding.etPassword.error = getString(R.string.password_salah)
        Toast.makeText(this, R.string.gagal_login, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.logoApp, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val messageAnimator = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(300)
        val titleEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(300)
        val emailAnimator = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(300)
        val titlePassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val passwordAnimator = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(300)
        val checkBoxAnimator = ObjectAnimator.ofFloat(binding.checkBox, View.ALPHA, 1f).setDuration(300)
        val registerText = ObjectAnimator.ofFloat(binding.tvAskAkun, View.ALPHA, 1f).setDuration(300)
        val loginButtonAnimator = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                messageAnimator,
                titleEmail,
                emailAnimator,
                titlePassword,
                passwordAnimator,
                checkBoxAnimator,
                registerText,
                loginButtonAnimator
            )
            start()
        }
    }

    private fun setAlphaToZero() {
        val views = listOf( binding.etEmail, binding.etPassword, binding.btnLogin,
            binding.tvEmail, binding.tvPassword, binding.tvWelcome, binding.tvAskAkun,
            binding.checkBox
        )

        views.forEach { view ->
            ObjectAnimator.ofFloat(view, "alpha", 0f).apply {
                duration = 0
                start()
            }
        }
    }
}
