package com.ajiswn.dicodingstory.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ajiswn.dicodingstory.R
import com.ajiswn.dicodingstory.ViewModelFactory
import com.ajiswn.dicodingstory.data.pref.UserModel
import com.ajiswn.dicodingstory.databinding.ActivityLoginBinding
import com.ajiswn.dicodingstory.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupAction()
        playAnimation()
        observeLoginResponse()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val isPasswordValid = password.length >= 8

            if (email.isEmpty() || !isEmailValid) {
                showToast(getString(R.string.error_invalid_email))
            } else if (password.isEmpty() || !isPasswordValid) {
                showToast(getString(R.string.error_password_short))
            } else {
                viewModel.login(email, password)
            }
        }
    }

    private fun observeLoginResponse() {
        viewModel.loginResponse.observe(this) { loginResponse ->
            if (loginResponse.error == true) {
                showToast(loginResponse.message ?: getString(R.string.register_failed_message))
            } else {
                loginResponse.loginResult?.let { loginResult ->
                    viewModel.saveSession(
                        UserModel(
                            email = binding.edLoginEmail.text.toString(),
                            token = loginResult.token
                        )
                    )
                    showSuccessDialog(loginResult.name)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showSuccessDialog(email: String) {
        AlertDialog.Builder(this@LoginActivity).apply {
            setTitle(getString(R.string.login_success_title))
            setMessage(getString(R.string.login_success_message, email))
            setPositiveButton(getString(R.string.next_button)) { _, _ ->
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailEdit,
                passwordEdit,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}