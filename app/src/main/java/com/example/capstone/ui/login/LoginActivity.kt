package com.example.capstone.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.MainActivity
import com.example.capstone.data.ViewModelFactory
import com.example.capstone.data.pref.UserModel
import com.example.capstone.data.pref.UserPreference
import com.example.capstone.data.pref.dataStore
import com.example.capstone.databinding.ActivityLoginBinding
import com.example.capstone.ui.signUp.SignUpActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.clickableTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        setupAction()
        checkSession()
    }


    private fun setupAction(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().length
            val password = binding.passwordEditText.text.toString().length
            if (email != 0 && password != 0){
                setupLogin()
            }else{
                showToast("Isi data dengan benar!")
            }
        }
    }

    private fun setupLogin() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()
        showLoading(true)
        val viewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: LoginViewModel by viewModels { viewModelFactory }

        viewModel.loginUser(email, password)
        viewModel.loginResponse.observe(this) { response ->
            val message = response?.message ?: "Unknown error"
            val token = response?.token

            if (token != null && message.contains("success", ignoreCase = true)) {
                viewModel.saveSession(
                    UserModel(
                        email = binding.emailEditText.text.toString(),
                        password = binding.passwordEditText.text.toString(),
                        token = token,
                        isLogin = true
                    )
                )
                showLoading(false)
                goToHomeActivity()
            } else {
                showLoading(false)
                showToast("Error, $message")
            }
        }
    }

        private fun checkSession() {
        val userPreference = UserPreference.getInstance(dataStore)
        CoroutineScope(Dispatchers.Main).launch {
            if (userPreference.isSessionActive()) {
                showLoading(false)
                goToHomeActivity()
            }
        }
    }
    private fun goToHomeActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}