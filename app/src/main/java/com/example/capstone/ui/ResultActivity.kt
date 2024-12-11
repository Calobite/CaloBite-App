package com.example.capstone.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.data.response.ListDataFood
import com.example.capstone.ui.detailFood.DetailFoodActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        supportActionBar?.title = "Result"

        val foodList = intent.getParcelableArrayListExtra<ListDataFood>("FOOD_DATA")

        val recyclerView = findViewById<RecyclerView>(R.id.rv_result)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = FoodSearchAdapter(foodList ?: emptyList()) { food ->
            val intent = Intent(this, DetailFoodActivity::class.java)
            intent.putExtra("FOOD_DATA", food)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // Ganti `HomeActivity` dengan nama aktivitas utama Anda
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
