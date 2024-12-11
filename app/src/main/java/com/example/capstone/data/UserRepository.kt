package com.example.capstone.data

import FoodPagingSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.capstone.data.pref.UserModel
import com.example.capstone.data.pref.UserPreference
import com.example.capstone.data.response.DetailResponse
import com.example.capstone.data.response.FoodResponse
import com.example.capstone.data.response.ListDataFood
import com.example.capstone.data.response.PredictionResponse
import com.example.capstone.data.response.ResponseLogin
import com.example.capstone.data.response.ResponseRegister
import com.example.capstone.data.retrofit.ApiConfig
import com.example.capstone.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun userRegister( email: String, password: String): ResponseRegister {
        return try{
            apiService.register( email, password)
        } catch (e: Exception) {
            ResponseRegister( error = "not found", message = e.message?:"check your fill out")
        }
    }

    suspend fun userLogin(email: String, password: String): ResponseLogin {
        return apiService.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }


    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }
    private val apiServiceMl = ApiConfig.getApiServiceMl()
    suspend fun uploadImage(file: File): PredictionResponse {
        val requestFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestFile)
        return apiServiceMl.getPrediction(multipartBody)
    }

    suspend fun getFoodDetail(recipesId: String): Call<DetailResponse> {
        return apiService.getDetailFood(recipesId) // Assuming this method exists in ApiService
    }
    fun getFoodStream(): Flow<PagingData<ListDataFood>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FoodPagingSource(apiService) }
        ).flow
    }

    suspend fun searchFoodByIngredient(ingredient: String): retrofit2.Response<FoodResponse> {
        return apiService.searchFoodByIngredient(ingredient)
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService,userPreference)
            }.also { instance = it }
    }
}