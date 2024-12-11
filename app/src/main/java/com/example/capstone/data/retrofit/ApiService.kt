package com.example.capstone.data.retrofit

import com.example.capstone.data.response.DetailResponse
import com.example.capstone.data.response.FoodResponse
import com.example.capstone.data.response.NewsResponse
import com.example.capstone.data.response.PredictionResponse
import com.example.capstone.data.response.ResponseLogin
import com.example.capstone.data.response.ResponseRegister
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("food")
    suspend fun getFood(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): FoodResponse


    @GET("food/{recipesId}")
    fun getDetailFood(
        @Path("recipesId") recipesId: String? = null
    ): Call<DetailResponse>

    @GET("food")
    suspend fun searchFoodByIngredient
        (@Query("ingredients") ingredient: String
    ): Response<FoodResponse>


    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @Multipart
    @POST("predict")
    suspend fun getPrediction(
        @Part photo: MultipartBody.Part
    ): PredictionResponse

    @GET("top-headlines")
    fun getCancerArticles(
        @Query("q") query: String = "food",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = "118167aa520949ac9e179aaf81631884"
    ): Call<NewsResponse>

}