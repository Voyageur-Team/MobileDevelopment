package com.voyageur.application.view.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.voyageur.application.R
import com.voyageur.application.data.model.RegisterDataAccount
import com.voyageur.application.databinding.ActivityRegisterBinding
import com.voyageur.application.viewmodel.AuthViewModel

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        showLoading(false)
        setupObservers()
//        setupAnimation()

        binding.cbPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        binding.buttonRegister.setOnClickListener {
            binding.apply {
                etName.clearFocus()
                etEmail.clearFocus()
                etPassword.clearFocus()
                etConfirmPassword.clearFocus()
            }

            if (isRegisterFormValid()) {
                showLoading(true)
                val dataRegisterAccount = RegisterDataAccount(
                    name = binding.etName.text.toString().trim(),
                    email = binding.etEmail.text.toString().trim(),
                    password = binding.etPassword.text.toString().trim()
                )
                authViewModel.register(dataRegisterAccount)
            } else {
                showRegisterError()
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        navigateToLogin()
    }

    private fun setupObservers() {
        authViewModel.isLoading.observe(this) { showLoading(it) }
        authViewModel.message.observe(this) { response ->
            response?.let { responseRegister() }
        }
    }

    private fun isRegisterFormValid(): Boolean {
        return binding.etName.isNameValid &&
                binding.etEmail.isEmailValid &&
                binding.etPassword.isPasswordValid &&
                binding.etConfirmPassword.isPasswordValid
    }

    private fun showRegisterError() {
        if (!binding.etName.isNameValid) binding.etName.error = getString(R.string.nama_kosong)
        if (!binding.etEmail.isEmailValid) binding.etEmail.error = getString(R.string.email_kosong)
        if (!binding.etPassword.isPasswordValid) binding.etPassword.error = getString(R.string.password_kosong)
        if (!binding.etConfirmPassword.isPasswordValid) binding.etConfirmPassword.error = getString(
            R.string.password_tidak_sama)
        Toast.makeText(this, R.string.login_gagal, Toast.LENGTH_SHORT).show()
    }

    private fun responseRegister() {
        authViewModel.isError.observe(this) { isError ->
            val message = authViewModel.message.value
            if (!isError) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                navigateToLogin()
            } else {
                if (message == "1") {
                    Toast.makeText(this, resources.getString(R.string.email_digunakan), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, message ?: getString(R.string.register_gagal), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.logoApp, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val messageAnimator = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val nameAnimator = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(500)
        val emailAnimator = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val passwordAnimator = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val passwordConAnimator = ObjectAnimator.ofFloat(binding.etConfirmPassword, View.ALPHA, 1f).setDuration(500)
        val loginButtonAnimator = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                messageAnimator,
                nameAnimator,
                emailAnimator,
                passwordAnimator,
                passwordConAnimator,
                loginButtonAnimator
            )
            start()
        }
    }
}