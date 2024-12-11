package com.example.capstone.ui.analyze

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import com.example.capstone.data.response.FoodResponse
import com.example.capstone.data.response.PredictionResponse
import kotlinx.coroutines.launch
import java.io.File

class AnalyzeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _predictionResult = MutableLiveData<PredictionResponse?>()
    val predictionResult: LiveData<PredictionResponse?> get() = _predictionResult

    private val _foodSearchResult = MutableLiveData<FoodResponse?>()
    val foodSearchResult: LiveData<FoodResponse?> get() = _foodSearchResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun uploadImage(file: File) {
        viewModelScope.launch {
            try {
                val response = userRepository.uploadImage(file)
                _predictionResult.postValue(response)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun searchFoodByIngredient(ingredient: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.searchFoodByIngredient(ingredient)
                if (response.isSuccessful) {
                    _foodSearchResult.postValue(response.body())
                } else {
                    _foodSearchResult.postValue(null)
                    _error.postValue("No recipes found for ingredient: $ingredient")
                }
            } catch (e: Exception) {
                _foodSearchResult.postValue(null)
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}