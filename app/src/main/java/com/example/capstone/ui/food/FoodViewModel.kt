package com.example.capstone.ui.food

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import com.example.capstone.data.response.ListDataFood
import com.example.capstone.data.retrofit.ApiConfig
import kotlinx.coroutines.launch


class FoodViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _foodData = MutableLiveData<List<ListDataFood>>()
    val dataFood: LiveData<List<ListDataFood>> = _foodData

    val foodStream = userRepository.getFoodStream()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    companion object {
        private const val TAG = "FoodViewModel"
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_SIZE = 10
    }

    init {
        fetchDataFood(DEFAULT_PAGE, DEFAULT_SIZE)
    }

    fun fetchDataFood(page: Int, size: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiServiceFood().getFood(page, size)
                Log.d("FoodViewModel", "Fetched food data: ${response.data}")
                _foodData.value = response.data
            } catch (e: Exception) {
                Log.e("FoodViewModel", "Error fetching food data: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}
