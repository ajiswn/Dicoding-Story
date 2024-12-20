package com.ajiswn.dicodingstory.view.signup

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
import com.ajiswn.dicodingstory.databinding.ActivitySignupBinding
import com.ajiswn.dicodingstory.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupAction()
        playAnimation()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val isPasswordValid = password.length >= 8

            if (name.isEmpty()) {
                binding.nameEditTextLayout.error = getString(R.string.error_invalid_name)
                showToast(getString(R.string.error_invalid_name))
            } else if (email.isEmpty() || !isEmailValid) {
                showToast(getString(R.string.error_invalid_email))
            } else if (password.isEmpty() || !isPasswordValid) {
                showToast(getString(R.string.error_password_short))
            } else {
                viewModel.register(name, email, password).observe(this) { registerResponse ->
                    if (registerResponse.error == true) {
                        showToast(registerResponse.message ?: getString(R.string.register_failed_message))
                    } else {
                        AlertDialog.Builder(this@SignupActivity).apply {
                            setTitle(getString(R.string.signup_success_title))
                            setMessage(getString(R.string.signup_success_message, email))
                            setPositiveButton(getString(R.string.next_button)) { _, _ ->
                                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }

            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameEdit,
                emailEdit,
                passwordEdit,
                signup
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