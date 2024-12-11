package com.example.capstone.data.di

import android.content.Context
import com.example.capstone.data.UserRepository
import com.example.capstone.data.pref.UserPreference
import com.example.capstone.data.pref.dataStore
import com.example.capstone.data.retrofit.ApiConfig

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiServiceFood()
        return UserRepository.getInstance(apiService,pref)
    }


}
