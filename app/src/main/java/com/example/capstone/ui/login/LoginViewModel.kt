package com.example.capstone.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import com.example.capstone.data.pref.UserModel
import com.example.capstone.data.response.ResponseLogin
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginResponse = MutableLiveData<ResponseLogin>()
    val loginResponse: LiveData<ResponseLogin> = _loginResponse

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.userLogin(email, password)

                val message = response.message
                val token = response.token

                if (token != null && message.contains("success", ignoreCase = true)) {
                    saveSession(UserModel(email, password, token, isLogin = true))
                    _loginResponse.value = response
                } else {
                    _loginResponse.value = response
                }
            } catch (e: Exception) {
                _loginResponse.value = ResponseLogin(
                    token = null,
                    error = "",
                    message = e.message ?: "Unknown error"
                )
            }
        }
    }



    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}