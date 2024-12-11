package com.example.capstone.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import com.example.capstone.data.response.ResponseRegister
import kotlinx.coroutines.launch

class SignUpViewModel (private val userRepository: UserRepository): ViewModel() {
    private val _registerResponse = MutableLiveData<ResponseRegister>()
    val registerResponse: LiveData<ResponseRegister> = _registerResponse

    fun registerUser( email: String, password: String) {
        viewModelScope.launch {
            val responseRegister = userRepository.userRegister( email, password)
            _registerResponse.value = responseRegister
        }
    }
}