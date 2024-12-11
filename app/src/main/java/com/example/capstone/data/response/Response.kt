package com.example.capstone.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FoodResponse (

    @field:SerializedName("data")
    val data: List<ListDataFood> = listOf(),

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("pagination")
    val pagination: Pagination,


)

@Parcelize
data class ListDataFood(

    @field:SerializedName("recipes_id")
    val recipesId: String? = null,

    @field:SerializedName("recipe_name")
    val recipeName: String? = null,

    @field:SerializedName("prep_time")
    val prepTime: String? = null,

    @field:SerializedName("cook_time")
    val cookTime: String? = null,

    @field:SerializedName("total_time")
    val totalTime: String? = null,

    @field:SerializedName("servings")
    val servings: String? = null,

    @field:SerializedName("rating")
    val rating: Float? = 0.0f,

    @field:SerializedName("img_src")
    val imgSrc: String? = null
): Parcelable

data class Pagination(
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)

data class DetailResponse (
    val recipes_id: String,
    val recipe_name: String,
    val prep_time: String,
    val cook_time: String,
    val total_time: String,
    val servings: String,
    val yield: String,
    val ingredients: String,
    val directions: String,
    val rating: String,
    val url: String,
    val cuisine_path: String,
    val nutrition: String,
    val timing: String,
    val img_src: String
)


data class IngredientResponse(

    @field:SerializedName("Food")
    val food: String? = null,

    @field:SerializedName("Serving")
    val serving: String? = null,

    @field:SerializedName("Calories")
    val calories: String? = null,
)


//RESPONSE REGISTER
data class ResponseRegister (

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("message")
    val message: String? = null

)
//RESPONSE LOGIN
data class ResponseLogin(

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("message")
    val message: String
)



// ML MODEL
data class PredictionResponse(
    @field:SerializedName("predictions")
    val predictions: List<Prediction>
)

data class Prediction(
    @field:SerializedName("ymin")
    val ymin: Int,
    @field:SerializedName("xmin")
    val xmin: Int,
    @field:SerializedName("ymax")
    val ymax: Int,
    @field:SerializedName("xmax")
    val xmax: Int,
    @field:SerializedName("score")
    val score: Float,
    @field:SerializedName("label")
    val label: String
)

//News Article
data class NewsResponse(

    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("articles")
    val articles: List<ArticlesItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class Source(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Any? = null
)

data class ArticlesItem(

    @field:SerializedName("publishedAt")
    val publishedAt: String? = null,

    @field:SerializedName("author")
    val author: Any? = null,

    @field:SerializedName("urlToImage")
    val urlToImage: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("source")
    val source: Source? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("content")
    val content: String? = null
)