package com.example.capstone.ui.signUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.MainActivity
import com.example.capstone.data.ViewModelFactory
import com.example.capstone.data.pref.UserPreference
import com.example.capstone.data.pref.dataStore
import com.example.capstone.databinding.ActivitySignUpBinding
import com.example.capstone.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.clickableTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        setupAction()
        checkSession()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val emailLength = binding.emailEditText.text.toString().length
            val passwordLength = binding.passwordEditText.text.toString().length
            if (emailLength != 0 && passwordLength != 0) {
                setRegister()
            } else {
                showToast("Isi data dengan benar!")
            }
        }
    }

    private fun setRegister() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()
        showLoading(true)

        val viewModelFactory = ViewModelFactory.getInstance(this@SignUpActivity)
        val viewModel: SignUpViewModel by viewModels { viewModelFactory }

        viewModel.registerUser(email, password)

        viewModel.registerResponse.observe(this) { responseRegister ->
            val message = responseRegister?.message ?: "Unknown error"
            val token = responseRegister?.token
            val error = responseRegister?.error ?: "Unknown error"
            if (token != null && message.contains("registered successfully", ignoreCase = true)) {
                showLoading(false)
                showToast(message)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                showLoading(false)
                showToast("Terjadi Kesalahan: $error")
            }
        }
    }

    private fun checkSession() {
        val userPreference = UserPreference.getInstance(dataStore)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (userPreference.isSessionActive()) {
                    showLoading(false)
                    goToHomeActivity()
                }
            } catch (e: Exception) {
                Log.e("SignUpActivity", "Error checking session: ${e.message}")
            }
        }
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
