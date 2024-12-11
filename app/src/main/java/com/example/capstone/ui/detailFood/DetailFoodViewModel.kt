package com.example.capstone.ui.detailFood

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import com.example.capstone.data.response.DetailResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFoodViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _foodDetail = MutableLiveData<DetailResponse>()
    val foodDetail: LiveData<DetailResponse>  = _foodDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getFoodDetail(recipesId: String): LiveData<DetailResponse>{
        viewModelScope.launch {
            try {
                userRepository.getFoodDetail(recipesId).enqueue(object : Callback<DetailResponse>{
                    override fun onResponse(
                        call: Call<DetailResponse>,
                        response: Response<DetailResponse>
                    ) {
                        if (response.isSuccessful) {
                            _foodDetail.value = response.body()
                        } else {
                            Log.e("detailViewModel", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                        Log.e("detailViewModel", "Error: ${t.message}")
                    }
                })
            } catch (e: Exception){
                Log.e("detailViewModel", "Error: ${e.message}")
            }
        }
        return foodDetail
    }
}
