package com.example.capstone.ui.detailFood

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.capstone.data.ViewModelFactory
import com.example.capstone.data.di.Injection
import com.example.capstone.data.response.DetailResponse
import com.example.capstone.data.response.ListDataFood
import com.example.capstone.databinding.ActivityDetailFoodBinding

class DetailFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFoodBinding
    private lateinit var imgCover: ImageView
    private lateinit var recipeName: TextView
    private lateinit var valPreptime: TextView
    private lateinit var valCoktime: TextView
    private lateinit var valTottime: TextView
    private lateinit var valServings: TextView
    private lateinit var valRatingBar: RatingBar
    private lateinit var valIngredients: TextView
    private lateinit var valDirections: TextView
    private lateinit var valNutrition: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: DetailFoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userRepository = Injection.provideUserRepository(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(userRepository)).get(DetailFoodViewModel::class.java)

        // Observe LiveData
        viewModel.foodDetail.observe(this, { foodResponse ->
            foodResponse?.let {
                updateUi(it)
            } ?: run {
                Log.e("DetailFoodActivity", "Received foodDetail is null")
            }
        })

        viewModel.isLoading.observe(this, { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(this, { errorMessage ->
            errorMessage?.let {
                Log.e("DetailFoodActivity", "Error: $it")
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        val foodData = intent.getParcelableExtra<ListDataFood>("FOOD_DATA")
        if (foodData == null) {
            Log.e("DetailFoodActivity", "No FOOD_DATA found in Intent ${foodData}")
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Log.d("DetailFoodActivity", "Received FOOD_DATA: ${foodData.recipeName}")
            foodData.recipesId?.let { recipesId ->
                Log.d("DetailFoodActivity", "Requesting food detail for recipeId: $recipesId")
                viewModel.getFoodDetail(recipesId)
            }
        }

        setupUI()
    }

    private fun setupUI() {
        supportActionBar?.title = "Detail Food"
        imgCover = binding.imgCover
        recipeName = binding.recipeName
        valPreptime = binding.valPrepareTime
        valCoktime = binding.valCookTime
        valTottime = binding.valTotalTime
        valServings = binding.valServings
        valRatingBar = binding.valRating
        valIngredients = binding.valIngredients
        valDirections = binding.valDirections
        valNutrition = binding.valNutrition
        progressBar = binding.progressBar
    }

    private fun updateUi(detailResponse: DetailResponse) {
        Log.d("DetailFoodActivity", "Updating UI with food details: ${detailResponse.recipe_name}")

        recipeName.text = detailResponse.recipe_name
        valPreptime.text = detailResponse.prep_time
        valCoktime.text = detailResponse.cook_time
        valTottime.text = detailResponse.total_time
        valServings.text = detailResponse.servings
        val rating = detailResponse.rating ?.toFloatOrNull() ?: 0.0f
        valRatingBar.rating = rating
        valIngredients.text = detailResponse.ingredients
        valDirections.text = detailResponse.directions
        valNutrition.text = detailResponse.nutrition


        Glide.with(this)
            .load(detailResponse.img_src)
            .into(imgCover)
    }
}
