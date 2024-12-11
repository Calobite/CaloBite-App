package com.example.capstone.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstone.data.UserRepository
import com.example.capstone.data.pref.UserModel
import com.example.capstone.data.response.ArticlesItem
import com.example.capstone.data.response.NewsResponse
import com.example.capstone.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _newsList = MutableLiveData<List<ArticlesItem>>()
    val newsList: LiveData<List<ArticlesItem>> get() = _newsList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getCancerArticles() {
        _loading.value = true
        val apiService = ApiConfig.getApiServiceNews()
        apiService.getCancerArticles().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    val articles = response.body()?.articles?.filterNotNull() ?: emptyList()
                    _newsList.value = articles
                } else {
                    _newsList.value = emptyList()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _loading.value = false
                _newsList.value = emptyList()
            }
        })
    }


    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}