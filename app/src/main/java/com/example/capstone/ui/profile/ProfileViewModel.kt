package com.example.capstone.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel (private val userRepository: UserRepository): ViewModel(){

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}